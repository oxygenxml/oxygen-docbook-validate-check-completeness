package com.oxygenxml.docbook.checker.validator;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.common.base.Joiner;
import com.oxygenxml.docbook.checker.reporters.ProblemReporter;
import com.oxygenxml.docbook.checker.reporters.TabKey;
import com.oxygenxml.profiling.ProfilingConditionsInformations;
import com.oxygenxml.profiling.ProfilingConditionsInformationsImpl;

public class ConditionsChecker {

	private ProblemReporter problemReporter;

	public ConditionsChecker(ProblemReporter problemReporter) {
		this.problemReporter = problemReporter;

	}

	public void validateAndReport(String url, LinkedHashMap<String, LinkedHashSet<String>> conditions ) {
		ProfilingConditionsInformations profilingConditionsInformations = new ProfilingConditionsInformationsImpl();

		System.out.println("aiciiiiiiiii :" +conditions.toString());
			LinkedHashMap<String, LinkedHashSet<String>> definedConditions = profilingConditionsInformations
					.getProfileConditions(ProfilingConditionsInformations.ALL_DOCBOOKS);

			
			Iterator<String> iterKey = conditions.keySet().iterator();
			while (iterKey.hasNext()) {
				String key = iterKey.next();
				System.out.println("key : "+ key );
				
				if (definedConditions.containsKey(key)) {
					Iterator<String> iterValue = conditions.get(key).iterator();
					while (iterValue.hasNext()) {
						String value = iterValue.next();
						if (!definedConditions.get(key).contains(value)) {
							System.out.println("reportProblem: "+ key +" "+ value );
							problemReporter.reportUndefinedConditions(key, value,
									 TabKey.generate(url, ""));
						}
					}
				}
			}
	}
}
