package com.oxygenxml.docbook.checker.validator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

/**
 * Checker for external links and images using HttpURLConnection or URLConnection
 * @author Cosmin Duna
 *
 */
public class ExternalLinksAndImagesChecker {
	
	/**
	 * Private constructor.
	 */
	 private ExternalLinksAndImagesChecker() {
	    throw new IllegalStateException("Utility class");
	  }
	
	 /**
	  * A list with ignored protocols at validation.
	  */
	 private static final String[] IGNORED_PROTOCOLS =  new String[]{"mailto"};
	 
	/**
	 * Logger
	 */
	 private static final Logger logger = Logger.getLogger(ExternalLinksAndImagesChecker.class);
	
	/**
	 * Check if the given url is good or broken
	 * 
	 * @param url The URL of document.
	 * @throws IOException If the Url in broken.
	 */
	public static void check(URL url) throws IOException {
		String protocol = url.getProtocol();
		if(!isIgnoredProtocol(protocol)) {
			//check the protocol of given URL
			if ("http".equals(protocol) || "https".equals(protocol)) {
				HttpURLConnection huc = (HttpURLConnection) url.openConnection();

				//this will give a exception if the URL is broken
				huc.getResponseMessage();

				huc.disconnect();

			} else {
				URLConnection urlCon = url.openConnection();
				// this will give a exception if the URL is broken 
				InputStream is = urlCon.getInputStream();

				//close InputStream
				try {
					is.close();
				} catch (Exception e) {
					logger.debug(e.getMessage(), e);
				}
			}
		}
	}
	
	/**
	 * Check if the given protocol is ignored.
	 * @param protocol Protocol to be check
	 * @return <code>true</code> if the protocol should be ignored, <code>false</code> otherwise.
	 */
	private static boolean isIgnoredProtocol(String protocol) {
		boolean toRet = false;
		for (int i = 0; i < IGNORED_PROTOCOLS.length; i++) {
			if(IGNORED_PROTOCOLS[i].equals(protocol)) {
				toRet = true;
				break;
			}
		}
		return toRet;
	}
}
