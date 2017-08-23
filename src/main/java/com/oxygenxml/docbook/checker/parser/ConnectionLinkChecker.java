package com.oxygenxml.docbook.checker.parser;

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
			}
		}
	}
}
