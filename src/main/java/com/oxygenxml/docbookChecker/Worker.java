package com.oxygenxml.docbookChecker;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.oxygenxml.docbookChecker.reporters.LinksReporter;
import com.oxygenxml.ldocbookChecker.parser.Id;
import com.oxygenxml.ldocbookChecker.parser.Link;
import com.oxygenxml.ldocbookChecker.parser.LinksChecker;
import com.oxygenxml.ldocbookChecker.parser.LinksCheckerImp;
import com.oxygenxml.ldocbookChecker.parser.LinkDetails;
/**
 * 
 *Worker responsible with background process.
 *
 */
public class Worker  extends SwingWorker<List<Link> , Statistic> implements WorkerReporter {
	
	private List<URL> urls;

	private LinksChecker linkChecker;
	
	private LinksReporter linkReporter;
	
	private boolean checkExternal;
	
	/**
	 * Constructor
	 * @param url
	 * @param brokenLinkReporter
	 * @param allowedHostNames
	
	 */
	public Worker(List<URL> urls, boolean checkExternal, LinksReporter linkReporter) {
		this.urls = urls;
		this.checkExternal = checkExternal;
		this.linkReporter = linkReporter;
		linkChecker = new LinksCheckerImp();
	}
	
	
	@Override
	public List<Link> doInBackground() throws Exception {
		List<Link> results = new ArrayList<Link>();
		for(int i = 0; i < urls.size(); i++ ){
			//start check 
			results.addAll( linkChecker.check(urls.get(i) , checkExternal) );
		}
			return results;
	}
	
	
	@Override
	public void update(Statistic statistic) {
		publish(statistic);
		
	}
 
//	@Override
//	protected void process(List<Statistic> chunks) {
//		
//		for(int i = 0; i < chunks.size(); i++){
//			
//			if(chunks.get(i).isBroken() == null){
//				//update in view 
//				statisticReporter.reportInProcess(chunks.get(i).getNoToProcess());
//				statisticReporter.reportCheckedSize(chunks.get(i).getNoOfChecked());
//			
//			}else if(chunks.get(i).isBroken() == true){
//				linkReporter.reportBrokenLink(chunks.get(i).getLink(), chunks.get(i).getEx());
//			
//			}else{
//				linkReporter.reportCheckedLink(chunks.get(i).getLink());
//			}
//		}
//	}


	@Override
	protected void done() {
		try {
			List<Link> resuls = get();
			linkReporter.reportFinish(resuls);
			
			System.out.println("results:" + resuls.toString());
		
		} catch (InterruptedException | ExecutionException e) {
			//TODO afiseaza ceva
		}
		
	}
	
	
}

