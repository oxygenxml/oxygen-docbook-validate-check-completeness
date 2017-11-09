package com.oxygenxml.docbook.checker;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.jidesoft.utils.Base64.InputStream;

public class TestNpeClass {

	public void test() throws IOException{
		CheckerInteractor checkerInteractor  = null;

		checkerInteractor.getOtherFilesToCheck();
		
		URL url;
		try {
			url = new URL("file:/D:/docbook-validate-check-completeness/test-samples/broken-image/testdb4.xml");
			InputStream inputStream = (InputStream) url.openStream();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
