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

import ro.sync.util.PlatformDetector;

/**
 * Profile conditions finder.
 * @author intern4
 *
 */
public class ProfileConditionsFinder implements ProfilingInformation{

	ParserCreator parserCreator ;
	
	public ProfileConditionsFinder(ParserCreator parserCreator) {
		this.parserCreator = parserCreator;
	}
	
	@Override
	public Set<String> getProfileConditionAttributesNames(String documentType)
			throws ParserConfigurationException, SAXException, IOException {
		
		ProfileConditionsAttributesNamesFinderHandler userhandler = new ProfileConditionsAttributesNamesFinderHandler(documentType);
			
		InputSource is = getInputSource();
	
		XMLReader xmlReader = parserCreator.createXMLReader();
		
		xmlReader.setContentHandler(userhandler);
		xmlReader.parse(is);
		
		//oxyOptionsSa19.0.xml doesn't contain the information.
		if(userhandler.getResuts().isEmpty()){
			is = new InputSource("file:\\D:/docbook-validate-check-completeness/defaultProfileCondition.xml");
			xmlReader.parse(is);
		}
		
		return userhandler.getResuts();
	}

	@Override
	public Map<String, Set<String>> getProfileConditions(String documentType) throws ParserConfigurationException, SAXException, IOException {
		ProfileConditionsFinderHandler userhandler = new ProfileConditionsFinderHandler(documentType);
		
		InputSource is = getInputSource();
		
		XMLReader xmlReader = parserCreator.createXMLReader();
		
		xmlReader.setContentHandler(userhandler);
		
		xmlReader.parse(is);
		
		//oxyOptionsSa19.0.xml doesn't contain the information.
		if(userhandler.getResuts().isEmpty()){
			is = new InputSource("file:\\D:/docbook-validate-check-completeness/defaultProfileCondition.xml");
			xmlReader.parse(is);
		}
		
		return userhandler.getResuts();
	}

	@Override
	public Map<String, Map<String, Set<String>>> getConditionsSets(String documentType) throws ParserConfigurationException, SAXException, IOException {
		ProfileConditionsSetsFinderHandler userhandler = new ProfileConditionsSetsFinderHandler(documentType);
		
		InputSource is = getInputSource();
		
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

/**
 * Get InputSource of the document that contains profile conditions.	
 * @return The inputSource
 */
	private InputSource getInputSource(){
		InputSource is = new InputSource(System.getProperty("user.home") + "/AppData/Roaming/com.oxygenxml/oxyOptionsSa19.0.xml");
		
		 if (PlatformDetector.isWinXP()){
			 is = new InputSource(System.getProperty("user.home") + "\\Application Data\\com.oxygenxml\\oxyOptionsSa19.0.xml");
		 }
		 else if(PlatformDetector.isMacOS()){
			  is = new InputSource(System.getProperty("user.home") + "/Library/Preferences/com.oxygenxml/oxyOptionsSa19.0.xml");
		 }
		 else if(PlatformDetector.isLinux()){
			 is = new InputSource(System.getProperty("user.home") + "/.com.oxygenxml/oxyOptionsSa19.0.xml");
		 }
		 
		 return is;
	}
}
