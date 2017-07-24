package com.oxygenxml.ldocbookChecker.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Content getter from a given url.
 * 
 * @author intern4
 *
 */
public class ContentGetter {

	/**
	 * Get the content from a given URL
	 * 
	 * @param url
	 *          the Url
	 * @return the content
	 * @throws IOException
	 */
	public static InputStream openStream(URL url) throws IOException {
		InputStream toReturn = null;
		URLConnection urlConnection;

		// open connection
		urlConnection = url.openConnection();

		// get url content
		toReturn = urlConnection.getInputStream();

		return toReturn;
	}
}
