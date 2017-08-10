package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.oxygenxml.docbookChecker.CheckerInteractor;
import com.oxygenxml.docbookChecker.WorkerReporter;
import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.docbookChecker.reporters.StatusReporter;
import com.oxygenxml.profiling.ProfilingConditionsInformations;
import com.oxygenxml.profiling.ProfilingConditionsInformationsImpl;

/**
 * Checker for external links
 * 
 * @author intern4
 *
 */
public class LinksCheckerImp implements LinksChecker {

	boolean threadInterrupted = false;

	
	float progress = 0;
	
	boolean isFinalCycle = false;
	
	/**
	 * Check links at a given urls.
	 */
	@Override
	public void check(ParserCreator parserCreator, List<String> urls, CheckerInteractor interactor,
			ProblemReporter problemReporter, StatusReporter statusReporter, WorkerReporter workerReporter) 
	{
		
			// profile conditions from user
			Map<String, Map<String, Set<String>>> guiConditionsSets = new HashMap<String, Map<String,Set<String>>>();
			
			if(interactor.isSelectedCheckUsingProfile()){
				if(interactor.isSelectedConfigConditionsSet()){
					guiConditionsSets.put("set", interactor.getConditionsTableRows());
				}
				else{
					ProfilingConditionsInformations profilingConditionsInformations = new ProfilingConditionsInformationsImpl();
						guiConditionsSets.putAll(profilingConditionsInformations.getConditionsSets(ProfilingConditionsInformations.DOCBOOK));
				}
			}
			
			int nuOfCurrentSet = 0;
			String message = "";
			int nuOfSets = guiConditionsSets.size();
			
			Iterator<String> iterKey = guiConditionsSets.keySet().iterator();
			while(iterKey.hasNext()){
				if(threadInterrupted){
					break;
				}
				if(nuOfSets >1){
					nuOfCurrentSet++;
					
					message = "("+nuOfCurrentSet+"/"+nuOfSets+" sets) ";
					System.out.println("message++++++++++++++++++++++++++++ : "+message);
				}
				if(nuOfCurrentSet == nuOfSets){
					isFinalCycle = true;
				}
				
				
				String key = iterKey.next();
				Map<String, Set<String>> guiConditions = guiConditionsSets.get(key);
			
				checkUsingConditionsSet(guiConditions, message, parserCreator, urls,  interactor, problemReporter,  statusReporter,  workerReporter);
			}
	}
	


	private void checkUsingConditionsSet(Map<String, Set<String>> guiConditions, String message, ParserCreator parserCreator, List<String> urls, CheckerInteractor interactor,
			ProblemReporter problemReporter, StatusReporter statusReporter, WorkerReporter workerReporter) {
		try {
		statusReporter.reportStatus(StatusReporter.progress);
		
		LinksFinder linksFinder = new LinksFinderImpl();
		
		LinkDetails toProcessLinks = new LinkDetails();
		
		
		for (int i = 0; i < urls.size(); i++) {
			
			workerReporter.reportInProcessElement(message+"Parse file: " + urls.get(i).toString());
			
			toProcessLinks = toProcessLinks.add(linksFinder.gatherLinks(parserCreator, urls.get(i), guiConditions ,interactor));
			
			progress =  ((i + 1)*5 / urls.size());
			workerReporter.reporteProgress((int) progress, isFinalCycle);
			System.out.println("progres dupa raport: " + progress);
			
			if(Thread.interrupted() || threadInterrupted){
				threadInterrupted = true;
				System.out.println("*******************threadInterrupted "+ threadInterrupted);
				break;
			}
		}
		
		System.out.println("============================== " +toProcessLinks.getExternalLinks().toString());
		
		
		
		//------ check external links
		if (interactor.isSelectedCheckExternal() && !threadInterrupted) {
			checkExternalLinks(message, toProcessLinks, problemReporter, guiConditions, workerReporter);
		}
		System.out.println("check external finish");
		
		
		//------- check images
		if (interactor.isSelectedCheckImages() && !threadInterrupted) {
			checkImgLinks(message, toProcessLinks, problemReporter, guiConditions, workerReporter);
		}
		System.out.println("check image finish");
		
		
		//-------- check internal links
		if (interactor.isSelectedCheckInternal() && !threadInterrupted) {
			checkInternalLinks(message, toProcessLinks, problemReporter, guiConditions, workerReporter);
		}
		System.out.println("check internal finish");
		
		//report success status 
		statusReporter.reportStatus(StatusReporter.succes);
		
	} catch (SAXException e) {
		System.err.println("catch");
		problemReporter.reportException(e);
		statusReporter.reportStatus(StatusReporter.fail);
	} catch (ParserConfigurationException e) {
		System.err.println("catch");
		
		statusReporter.reportStatus(StatusReporter.fail);
		problemReporter.reportException(e);
	} catch (IOException e) {
		System.err.println("catch");
		
		statusReporter.reportStatus(StatusReporter.fail);
		problemReporter.reportException(e);
	}
	
}


		

	/**
	 * Check external links.
	 */
	private void checkExternalLinks(String message, LinkDetails toProcessLinks, ProblemReporter problemReporter,
			Map<String, Set<String>> guiConditions, WorkerReporter workerReporter) {

		
		Iterator<Link> iter = toProcessLinks.getExternalLinks().iterator();
		
		System.out.println("external links(linkCheckerImpl): "+  toProcessLinks.getExternalLinks().toString());
		
		
		while (iter.hasNext() ) {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e1) {
				// TODO Delete
				e1.printStackTrace();
			}
			Link link = (Link) iter.next();

			if (!link.isFilter(guiConditions)) {

				workerReporter.reportInProcessElement(message+"Check external link: " + link.getRef());

				try {
					ConnectionLinkChecker.check(link.getRef());
					
				} catch (IOException e) {
					e.printStackTrace();
					link.setType(LinkType.EXTERNAL);
					link.setException(e);
					problemReporter.reportBrokenLinks(link);
				}

			}
			
			progress +=   toProcessLinks.getExternalProgress()* 95 ;
			System.out.println("external progress ratia: " + toProcessLinks.getExternalProgress()+" progres inainte de raport: " +  progress);
			workerReporter.reporteProgress( (int) progress, isFinalCycle );
			if(Thread.interrupted()){
				threadInterrupted = true;
				System.out.println("*******************threadInterrupted "+ threadInterrupted);

				break;
			}
		}
	}

	/**
	 * Check images.
	 */
	private void checkImgLinks(String message, LinkDetails toProcessLinks, ProblemReporter problemReporter,
			Map<String, Set<String>> guiConditions, WorkerReporter workerReporter) {

		Iterator<Link> iter = toProcessLinks.getImgLinks().iterator();
		while (iter.hasNext() ) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e1) {
				// TODO delete
				e1.printStackTrace();
			}
			Link link = (Link) iter.next();
			if (!link.isFilter(guiConditions)) {

				workerReporter.reportInProcessElement(message+"Check image: " + link.getRef());

					try {
						ConnectionLinkChecker.check(link.getAbsoluteLocation().toString());
						
				} catch (IOException e) {
					link.setType(LinkType.IMAGE);
					link.setException(e);
					problemReporter.reportBrokenLinks(link);
				}
			}

			progress +=   toProcessLinks.getImageProgress()* 95 ;
			System.out.println("progres dupa raport: " +  progress);
			workerReporter.reporteProgress( (int) progress, isFinalCycle );
		
			if(Thread.interrupted()){
				threadInterrupted = true;
				System.out.println("*******************threadInterrupted "+ threadInterrupted);
				break;
			}
		}
		
	}

	/**
	 * Check internal links.
	 */
	private void checkInternalLinks(String message, LinkDetails toProcessLinks, ProblemReporter problemReporter,
			Map<String, Set<String>> guiConditions, WorkerReporter workerReporter) {

		
		List<Id> paraIds = toProcessLinks.getParaIds();
		System.out.println("paraID***********:" + paraIds.toString());
		Iterator<Link> iter = toProcessLinks.getInternalLinks().iterator();
		while (iter.hasNext()) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e1) {
				// TODO delete
				e1.printStackTrace();
			}
			Link link = (Link) iter.next();

			// test if link is filter
			if (!link.isFilter(guiConditions)) {

				workerReporter.reportInProcessElement(message+"Check internal link: " + link.getRef());

				// check if the list with IDs doesn't contain the reference of link.
				Boolean linkPoints = linkPointsToID(paraIds, link, guiConditions);

				if (linkPoints == null) {
					link.setType(LinkType.INTERNAL);
					link.setException(new Exception("ID: " + link.getRef() + " not found"));
					problemReporter.reportBrokenLinks(link);
				} else if (false == linkPoints) {
					link.setType(LinkType.INTERNAL);
					link.setException(new Exception("Reference to ID " + link.getRef() + " defined in filtered out content."));
					problemReporter.reportBrokenLinks(link);
				}

			}

			progress +=   toProcessLinks.getInternalProgress()* 95 ;
			System.out.println("progres dupa raport: " +  progress);
			workerReporter.reporteProgress((int) progress , isFinalCycle);
			
			if(Thread.interrupted()){
				threadInterrupted = true;
				System.out.println("*******************threadInterrupted "+ threadInterrupted);
				break;
			}
		}
	}

	/**
	 * Check if the link refers at a valid id from the IDs list.
	 * 
	 * @param list
	 *          the list with ID's
	 * @param link
	 *          the reference(link)
	 * @return <code>true</code> if the link refers at a valid id ,
	 *         <code>false</code> if refers at a filtered id <code>null</code>> if
	 *         wasn't found the referred id in IDs list
	 */
	private Boolean linkPointsToID(List<Id> list, Link link, Map<String, Set<String>> conditionsTableGUI) {
		Boolean toReturn = null;

		// iterates over IDs list
		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).getId().equals(link.getRef())) {
				// was found the referred id
				// check if the id is filter
				if (!list.get(i).isFilter(conditionsTableGUI)) {
					//was found a valid id
					return true;
				} else {
					//id was found, but is filter
					toReturn = false;
				}
			}
		}
		return toReturn;
	}
}
