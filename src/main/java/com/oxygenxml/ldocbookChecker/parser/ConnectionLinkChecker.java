package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Checker for external links and images using HttpURLConnection or URLConnection
 * @author intern4
 *
 */
public class ConnectionLinkChecker {
	/**
	 * Check if the given URL is good or broken
	 * 
	 * @param url The URL in String format.
	 * @throws IOException 
	 */
	public static void check(String stringUrl) throws IOException {
		URL url = new URL(stringUrl);
		
		String protocol = url.getProtocol();
		
		//check the protocol of given URL
		if ("http".equals(protocol) || "https".equals(protocol)) {
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			
			//TODO
			//huc.setConnectTimeout(timeout);

			//this will give a exception if the URL is broken
			huc.getResponseMessage();

			//TODO disconnect
//			huc.disconnect();

//      huc.setRequestMethod("HEAD");
//			huc.getContentType();
//			
//			//get response
//			int status = huc.getResponseCode();
//	
//			//test response
//			if (huc.getResponseCode() != HttpURLConnection.HTTP_OK) {
//				
//				//http 301 or 302 (redirect)
//				if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER) {
//					
//					//check the redirected link
//					check(huc.getHeaderField("Location"));
//					
//				} else {throw new HTTPException(" HTTP: " + huc.getResponseCode() +" : "+ huc.getResponseMessage() + " : " +huc.getContentType());
//				}
//			}

		} else {
			URLConnection urlCon = url.openConnection();
			// this will give a exception if the URL is broken 
			urlCon.getInputStream();
//TODO close stream.
		}
	}
}
