package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.oxygenxml.docbookChecker.CheckerInteractor;
import com.oxygenxml.docbookChecker.WorkerReporter;
import com.oxygenxml.docbookChecker.reporters.ProblemReporter;
import com.oxygenxml.docbookChecker.reporters.StatusReporter;

/**
 * Checker for external links
 * 
 * @author intern4
 *
 */
public class LinksCheckerImp implements LinksChecker {

	final String PARSE = "parse";

	final String CHECK = "check";


	boolean threadInterrupted = false;

	
	float progress = 0;
	
	
	/**
	 * Check links at a given urls.
	 */
	@Override
	public void check(ParserCreator parserCreator, List<String> urls, CheckerInteractor interactor,
			ProblemReporter problemReporter, StatusReporter statusReporter, WorkerReporter workerReporter) 
	{
		try {
		
			statusReporter.reportStatus(StatusReporter.progress);

			LinksFinder linksFinder = new LinksFinderImpl();

			LinkDetails toProcessLinks = new LinkDetails();


			for (int i = 0; i < urls.size(); i++) {

				workerReporter.reportInProcessElement("Parse file: " + urls.get(i).toString());

				toProcessLinks = toProcessLinks.add(linksFinder.gatherLinks(parserCreator, urls.get(i), interactor));

				progress =  ((i + 1)*5 / urls.size());
				workerReporter.reporteProgress(Math.round(progress));
				System.out.println("progres dupa raport: " + progress);
				
				if(Thread.interrupted()){
					threadInterrupted = true;
					break;
				}
			}

			System.out.println("============================== " +toProcessLinks.getExternalLinks().toString());

			// profile conditions from user
			Map<String, Set<String>> guiConditions = interactor.getConditionsTableRows();

			
			//------ check external links
			if (interactor.isSelectedCheckExternal() && !threadInterrupted) {
				checkExternalLinks(toProcessLinks, problemReporter, guiConditions, workerReporter);
			}
			System.out.println("check external finish");

			
			//------- check images
			if (interactor.isSelectedCheckImages() && !threadInterrupted) {
				checkImgLinks(toProcessLinks, problemReporter, guiConditions, workerReporter);
			}
			System.out.println("check image finish");

			
			//-------- check internal links
			if (interactor.isSelectedCheckInternal() && !threadInterrupted) {
				checkInternalLinks(toProcessLinks, problemReporter, guiConditions, workerReporter);
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
	private void checkExternalLinks(LinkDetails toProcessLinks, ProblemReporter problemReporter,
			Map<String, Set<String>> guiConditions, WorkerReporter workerReporter) {

		
		Iterator<Link> iter = toProcessLinks.getExternalLinks().iterator();
		
		System.out.println("external links(linkCheckerImpl): "+  toProcessLinks.getExternalLinks().toString());
		
		
		while (iter.hasNext() ) {
			Link link = (Link) iter.next();

			if (!link.isFilter(guiConditions)) {

				workerReporter.reportInProcessElement("Check external link: " + link.getRef());

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
			System.out.println("progres dupa raport: " +  progress);
			workerReporter.reporteProgress( Math.round( progress) );
			if(Thread.interrupted()){
				threadInterrupted = true;
				break;
			}
		}
	}

	/**
	 * Check images.
	 */
	private void checkImgLinks(LinkDetails toProcessLinks, ProblemReporter problemReporter,
			Map<String, Set<String>> guiConditions, WorkerReporter workerReporter) {

		Iterator<Link> iter = toProcessLinks.getImgLinks().iterator();
		while (iter.hasNext() ) {
			Link link = (Link) iter.next();
			if (!link.isFilter(guiConditions)) {

				workerReporter.reportInProcessElement("Check image: " + link.getRef());

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
			workerReporter.reporteProgress( Math.round( progress) );
		
			if(Thread.interrupted()){
				threadInterrupted = true;
				break;
			}
		}
		
	}

	/**
	 * Check internal links.
	 */
	private void checkInternalLinks(LinkDetails toProcessLinks, ProblemReporter problemReporter,
			Map<String, Set<String>> guiConditions, WorkerReporter workerReporter) {

		
		List<Id> paraIds = toProcessLinks.getParaIds();
		System.out.println("paraID***********:" + paraIds.toString());
		Iterator<Link> iter = toProcessLinks.getInternalLinks().iterator();
		while (iter.hasNext()) {
			Link link = (Link) iter.next();

			// test if link is filter
			if (!link.isFilter(guiConditions)) {

				workerReporter.reportInProcessElement("Check internal link: " + link.getRef());

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
			workerReporter.reporteProgress( Math.round( progress) );
			
			if(Thread.interrupted()){
				threadInterrupted = true;
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
