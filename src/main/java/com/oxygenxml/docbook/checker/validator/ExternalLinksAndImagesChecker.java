package com.oxygenxml.docbook.checker.validator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checker for external links and images using HttpURLConnection or URLConnection
 * @author cosmin_duna
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
	 private static final Logger logger = LoggerFactory.getLogger(ExternalLinksAndImagesChecker.class);
	
	/**
	 * Check if the given url is good or broken
	 * 
	 * @param url The URL of document.
	 * @throws IOException If the Url in broken.
	 */
	public static void check(URL url) throws IOException {
		String protocol = url.getProtocol();
		if(!isIgnoredProtocol(protocol)) {
		  InputStream is = null;
      URLConnection openConnection = null; 
      try {
        openConnection = url.openConnection();
        boolean useStream = true;
        if (openConnection instanceof HttpURLConnection) {
          HttpURLConnection httpCon = (HttpURLConnection) openConnection;
          useStream = false;
          try {
            httpCon.setRequestMethod("HEAD");
            int status = httpCon.getResponseCode();
            if (status == HttpURLConnection.HTTP_FORBIDDEN 
                || status == HttpURLConnection.HTTP_NO_CONTENT
                || status == HttpURLConnection.HTTP_UNAVAILABLE
                || status == HttpURLConnection.HTTP_BAD_METHOD
                || status == -1) {
              useStream = true;
            } else if (status != HttpURLConnection.HTTP_OK) {
              StringBuilder message = new StringBuilder();
              message.append(status);
              message.append(' ');
              message.append(httpCon.getResponseMessage());
              message.append(" for: ").append(url.toExternalForm());
              
              throw new IOException(message.toString());
            }
          } finally {
            httpCon.disconnect();
          }
        }
        if (useStream) {
          is = openConnection.getInputStream();
          byte[] buffer = new byte[1];
          // Read from input stream
          is.read(buffer);
        }
      } finally {
        if (is != null) {
          try {
            // Close the input stream
            is.close();
          } catch (IOException ex) {
            logger.debug(ex.getMessage(), ex);
          }
        }

        if (openConnection instanceof HttpURLConnection) {
          ((HttpURLConnection) openConnection).disconnect();
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
