package tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.junit.Test;

import com.oxygenxml.docbook.checker.parser.Link;
import com.oxygenxml.docbook.checker.validator.DocumentChecker;
import com.oxygenxml.docbook.checker.validator.DocumentCheckerImp;

/**
 * JUnit for test the final hierarchy report.
 * 
 * @author intern4
 *
 */
public class HierarchyReportTest {

	@Test
	public void test() throws MalformedURLException {
		// Urls for testdb4 and test db5
		URL urlDb4 = new File("test-samples/hierarchy-report/db5/sampleXInclude.xml").toURI().toURL();
		URL section1 = new File("test-samples/hierarchy-report/db5/section1.xml").toURI().toURL();
		URL section3 = new File("test-samples/hierarchy-report/db5/section3.xml").toURI().toURL();
		
		DocumentChecker checker = new DocumentCheckerImp();

		// Problem reporters
		ProblemReporterImpl problemReporterDB4 = new ProblemReporterImpl();

		List<URL> urls = new ArrayList<URL>();
		urls.add(urlDb4);

		// start check
		DefaultMutableTreeNode root = checker.check(new PlainParserCreator(), new PlainProfilingConditionsInformations(),
				urls, new PlainCheckerInteractorImpl(false, new LinkedHashMap<String, String>()), problemReporterDB4,
				new StatusReporterImpl(), new PlainWorkerReporter(), new TranslatorImpl());

		TreeNode fileNode = root.getChildAt(0);

		// child of file node (external links and xi-includes)
		int childCount = fileNode.getChildCount();
		assertEquals(childCount, 2);

		// node -> external links
		TreeNode externalNode = fileNode.getChildAt(0);
		childCount = externalNode.getChildCount();
		assertEquals(childCount, 1);

		// node -> external links -> external link
		TreeNode externalLink = externalNode.getChildAt(0);
		assertEquals("http://www.google.com", ((Link) ((DefaultMutableTreeNode) externalLink).getUserObject()).getRef());

		// node -> xi-includes
		TreeNode xiIncludeNode = fileNode.getChildAt(1);
		childCount = xiIncludeNode.getChildCount();
		assertEquals(childCount, 2);

		// node -> xi-includes -> section1.xml
		TreeNode xiInclude1 = xiIncludeNode.getChildAt(0);
		assertEquals(section1.toString(),
				xiInclude1.toString());
		childCount = xiInclude1.getChildCount();
		assertEquals(childCount, 2);

		// node -> xi-includes -> section3.xml
		TreeNode xiInclude2 = xiIncludeNode.getChildAt(1);
		assertEquals(section3.toString(),
				xiInclude2.toString());
		childCount = xiInclude2.getChildCount();
		assertEquals(childCount, 1);

	}
}
