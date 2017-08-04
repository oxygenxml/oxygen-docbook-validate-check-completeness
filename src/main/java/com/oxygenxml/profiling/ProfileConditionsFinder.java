package com.oxygenxml.profiling;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.oxygenxml.ldocbookChecker.parser.OxygenParserCreator;
import com.oxygenxml.ldocbookChecker.parser.ParserCreator;
import com.oxygenxml.ldocbookChecker.parser.PlainParserCreator;


public class ProfileConditionsFinder implements ProfilingInformation{

	ParserCreator parserCreator ;
	
	public ProfileConditionsFinder(ParserCreator parserCreator) {
		this.parserCreator = parserCreator;
	}
	
	@Override
	public Set<String> getProfileConditionAttributesNames(String documentType)
			throws ParserConfigurationException, SAXException, IOException {
		
		ProfileConditionsAttributesNamesFinderHandler userhandler = new ProfileConditionsAttributesNamesFinderHandler(documentType);
	
		//TODO adauga linkuri pt mai multe SO
		InputSource is = new InputSource("file:\\C:\\Users\\intern4\\AppData\\Roaming\\com.oxygenxml\\oxyOptionsSa19.0.xml");
		
		XMLReader xmlReader = parserCreator.createXMLReader();
		
		xmlReader.setContentHandler(userhandler);
		xmlReader.parse(is);
		
		
		return userhandler.getResuts();
	}

	@Override
	public Map<String, Set<String>> getProfileConditions(String documentType) throws ParserConfigurationException, SAXException, IOException {
		ProfileConditionsFinderHandler userhandler = new ProfileConditionsFinderHandler(documentType);
		
		//TODO adauga linkuri pt mai multe SO
		InputSource is = new InputSource("file:\\C:\\Users\\intern4\\AppData\\Roaming\\com.oxygenxml\\oxyOptionsSa19.0.xml");
		
		XMLReader xmlReader = parserCreator.createXMLReader();
		
		xmlReader.setContentHandler(userhandler);
		
		xmlReader.parse(is);
		
		
		return userhandler.getResuts();
	}

	@Override
	public Map<String, Map<String, Set<String>>> getConditionsSets(String documentType) throws ParserConfigurationException, SAXException, IOException {
		ProfileConditionsSetsFinderHandler userhandler = new ProfileConditionsSetsFinderHandler(documentType);
		
		//TODO adauga linkuri pt mai multe SO
		InputSource is = new InputSource("file:\\C:\\Users\\intern4\\AppData\\Roaming\\com.oxygenxml\\oxyOptionsSa19.0.xml");
		
		XMLReader xmlReader = parserCreator.createXMLReader();
		
		xmlReader.setContentHandler(userhandler);
		
		xmlReader.parse(is);
		
		
		return userhandler.getResuts();
	}

	@Override
	public Map<String, Set<String>> getConditionsSet(List<String> urls) throws ParserConfigurationException, SAXException, IOException {
		ProfileDocsFinder finder = new ProfileDocsFinder();
		Map<String, Set<String>> toReturn = new HashMap<String, Set<String>>();
		for(int i =0; i < urls.size(); i++){
				toReturn.putAll(finder.gatherProfilingConditions(parserCreator, urls.get(i)));
			}
		return toReturn;
	}
	
//	public static void main(String[] args)  {
//		ProfileConditionsFinder finder = new ProfileConditionsFinder();
//		try {
//			System.out.println(finder.getConditionSets(ALLTYPES).toString());
//			
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		} catch (SAXException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

}
