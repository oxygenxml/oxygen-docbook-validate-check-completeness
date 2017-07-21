package com.oxygenxml.docbookChecker.reporters;

import java.util.List;

import com.oxygenxml.ldocbookChecker.parser.Link;
import com.oxygenxml.ldocbookChecker.parser.LinkDetails;

/**
 * Link reporter interface
 * @author intern4
 *
 */
public interface LinksReporter {
	/**
	 * Report finish check.
	 */
	void reportFinish(List<Link> results);
}
