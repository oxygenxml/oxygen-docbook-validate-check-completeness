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

import com.oxygenxml.docbook.checker.validator.DocumentChecker;
import com.oxygenxml.docbook.checker.validator.DocumentCheckerImp;

/**
 * Test if a xi-included document without references is added in the hierarchy report.
 * @author Cosmin Duna
 *
 */
public class HierarchyReportWithoutReferencesTest {

	@Test
	public void test() throws MalformedURLException {
			// Urls for testdb4 and test db5
			URL urlDb = new File("test-samples/hierarchy-report2/sampleXInclude.xml").toURI().toURL();
			URL section1 = new File("test-samples/hierarchy-report2/section1.xml").toURI().toURL();
			URL section2 = new File("test-samples/hierarchy-report2/section2.xml").toURI().toURL();
			
			DocumentChecker checker = new DocumentCheckerImp();

			// Problem reporters
			ProblemReporterImpl problemReporterDB4 = new ProblemReporterImpl();

			List<URL> urls = new ArrayList<URL>();
			urls.add(urlDb);

			// start check
			DefaultMutableTreeNode root = checker.check(new PlainParserCreator(), new PlainProfilingConditionsInformations(),
					urls, new PlainCheckerInteractorImpl(false, new LinkedHashMap<String, String>()), problemReporterDB4,
					new StatusReporterImpl(), new PlainWorkerReporter(), new TranslatorImpl());

			TreeNode fileNode = root.getChildAt(0);

			// child of document node  (xi-includes)
			int childCount = fileNode.getChildCount();
			assertEquals(1, childCount);

			// node -> xi-include (section1 and section2) 
			TreeNode xiInclude = fileNode.getChildAt(0);
			childCount = xiInclude.getChildCount();
			assertEquals(2, childCount);

			// node -> xi-include -> section1
			TreeNode nodeSection1 = xiInclude.getChildAt(0);
			assertEquals(section1, (URL)((DefaultMutableTreeNode) nodeSection1).getUserObject());
			//check the child count
			int childCountSection1 = nodeSection1.getChildCount();
			assertEquals(1, childCountSection1);

			
			// node -> xi-includes -> section2
			TreeNode nodeSection2 = xiInclude.getChildAt(1);
			assertEquals(section2, (URL)((DefaultMutableTreeNode) nodeSection2).getUserObject());
		
			//check the child count
			int childCountSection2 = nodeSection2.getChildCount();
			assertEquals(0, childCountSection2);

	}

}
