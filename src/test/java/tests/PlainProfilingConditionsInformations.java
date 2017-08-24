package tests;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.oxygenxml.profiling.ProfilingConditionsInformations;

public class PlainProfilingConditionsInformations implements ProfilingConditionsInformations {

	@Override
	public Set<String> getProfileConditionAttributesNames(String documentType) {
		Set<String> toReturn = new HashSet<String>();
		
		toReturn.add("os");
		toReturn.add("arch");
		
		return toReturn;
	}

	@Override
	public LinkedHashMap<String, LinkedHashSet<String>> getProfileConditions(String documentType) {
		
		LinkedHashMap<String, LinkedHashSet<String>> toReturn = new LinkedHashMap<String, LinkedHashSet<String>>();
		
		LinkedHashSet<String> set = new LinkedHashSet<String>();
		
		set.add("linux");
		set.add("mac");
		toReturn.put("os",set);
		
		set = new LinkedHashSet<String>();
		set.add("i486");
		set.add("i364");
		toReturn.put("arch", set);
	
		return toReturn;
	}

	@Override
	public LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<String>>> getConditionsSets(String documentType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkedHashMap<String, LinkedHashSet<String>> getConditionsFromDocs(String url)
			throws ParserConfigurationException, SAXException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getConditionSetsNames(String documentType) {
		// TODO Auto-generated method stub
		return null;
	}

}
