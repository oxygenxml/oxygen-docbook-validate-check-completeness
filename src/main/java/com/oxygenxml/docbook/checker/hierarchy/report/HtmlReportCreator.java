package com.oxygenxml.docbook.checker.hierarchy.report;

import java.io.File;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;

import com.oxygenxml.docbook.checker.parser.Link;
import com.oxygenxml.docbook.checker.parser.LinkType;

import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.util.XMLUtilAccess;

/**
 * Html report creator.
 * 
 * @author intern4
 *
 */
public class HtmlReportCreator {

	/**
	 * The firstPart of HTML.
	 */
	private String firstPart = "<!DOCTYPE html>\r\n" + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n"
			+ "    <head>\r\n" + "        <link rel=\"stylesheet\"\r\n"
			+ "            href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" />\r\n"
			+ "        <link rel=\"stylesheet\"\r\n"
			+ "            href=\"https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css\" />\r\n"
			+ "\r\n" + "    </head>\r\n" + "    <body>\r\n"
			+ "        <div style=\" position: absolute; left: 20px; top: 20px;\">\r\n"
			+ "            <input type=\"text\" id=\"search_div\" placeholder=\"Quick Search...\" class = \"form-control\"/>\r\n"
			+ "            <div id=\"jstree\">\r\n" + "                <ul>";

	/**
	 * The final part of HTML
	 */
	private String finalPart = "               </ul>\r\n" + "            </div>\r\n" + "        </div>\r\n"
			+ "        <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/1.12.1/jquery.min.js\"></script>\r\n"
			+ "        <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js\"></script>\r\n"
			+ "        <script>\r\n" + "            $(function () { \r\n" + "            $(\"#jstree\").jstree({\r\n"
			+ "             \"plugins\" : [ \"search\", \"types\" ]\r\n" + "                });\r\n"
			+ "             var to = false;\r\n" + "            $('#search_div').keyup(function () {\r\n"
			+ "             if(to) { clearTimeout(to); }\r\n" + "                to = setTimeout(function () {\r\n"
			+ "                  var v = $('#search_div').val();\r\n"
			+ "                 $('#jstree').jstree(true).search(v);\r\n" + "                }, 250);\r\n"
			+ "             });\r\n" + "            \r\n" + "            });\r\n" + "        </script>\r\n"
			+ "    </body>\r\n" + "</html>";

	/**
	 * The file icon
	 */
	private String FILE_ICON = "data-jstree='{\"icon\":\"glyphicon glyphicon-file\"}'";
	/**
	 * The link icon
	 */
	private String LINK_ICON = "data-jstree='{\"icon\":\"glyphicon glyphicon-link\"}'";

	/**
	 * XMLUtilAccess
	 */
	private XMLUtilAccess xmlUtilAccess = PluginWorkspaceProvider.getPluginWorkspace().getXMLUtilAccess();

	/**
	 * Convert the given tree in HTML.
	 * 
	 * @param root
	 *          The root of tree.
	 * @return The HTML content.
	 */
	public String convertToHtml(DefaultMutableTreeNode root, File outputFile) {
		return firstPart + convertToHtmlTree(root, outputFile) + finalPart;
	}

	private String convertToHtmlTree(DefaultMutableTreeNode root, File outputFile) {
		String toReturn = "<li ";

		Object rootObje = root.getUserObject();
		String text;
		String anchor;
		URL anchorUrl;

		// if the node is a Link
		if (rootObje instanceof Link) {
			Link link = (Link) rootObje;
			text = link.getRef();

			// get the anchor href
			if (link.getLinkType() != LinkType.INTERNAL) {
				anchorUrl = link.getAbsoluteLocation();
			} else {
				anchorUrl = link.getDocumentURL();
			}
			anchor = anchorUrl.toString();
			//relativize the path
			if (link.getLinkType() != LinkType.EXTERNAL) {
				try {
					anchor = Paths.get(outputFile.toURI()).relativize(Paths.get(anchorUrl.toURI())).toString();
				} catch (Throwable e1) {
				}
			}
			toReturn += LINK_ICON + " >" + "<a href=\"" + xmlUtilAccess.escapeAttributeValue(anchor) + "\">"
					+ xmlUtilAccess.escapeTextValue(text) + "</a>";
		}

		// if the node is a HierarchyReportStorageTreeNodeId (root node with a
		// condition set)
		else if (rootObje instanceof HierarchyReportStorageTreeNodeId) {
			HierarchyReportStorageTreeNodeId nodeId = (HierarchyReportStorageTreeNodeId) rootObje;
			anchor = nodeId.getDocumentUrl().toString();
			text = anchor.substring(anchor.lastIndexOf("/") + 1) + " - " + nodeId.getConditionSet();
			try {
				anchor = Paths.get(outputFile.toURI()).relativize(Paths.get(nodeId.getDocumentUrl().toURI())).toString();
			} catch (Throwable e1) {
			}
			toReturn += FILE_ICON + " >" + "<a href=\"" + xmlUtilAccess.escapeAttributeValue(anchor) + "\">"
					+ xmlUtilAccess.escapeTextValue(text) + "</a>";
		}

		// if the node is a URL
		else if (rootObje instanceof URL) {
			URL url = (URL) rootObje;
			anchor = url.toString();
			text = anchor.substring(anchor.lastIndexOf("/") + 1);
			try {
				anchor = Paths.get(outputFile.toURI()).relativize(Paths.get(url.toURI())).toString();
			} catch (URISyntaxException e) {
			} catch (Throwable e1) {
			}
			
			toReturn += FILE_ICON + " >" + "<a href=\"" + xmlUtilAccess.escapeAttributeValue(anchor) + "\">"
					+ xmlUtilAccess.escapeTextValue(text) + "</a>";
		} else {
			text = (String) rootObje;
			toReturn += " > " + text;
		}

		int childCount = root.getChildCount();
		if (childCount > 0) {
			toReturn += "<ul>";
		}

		for (int i = 0; i < childCount; i++) {
			toReturn += convertToHtmlTree((DefaultMutableTreeNode) root.getChildAt(i), outputFile);
		}

		if (childCount > 0) {
			toReturn += "</ul>";
		}

		return toReturn + "</li>";
	}

	/**
	 * Prettify and print the given content in given file.
	 * 
	 * @param content
	 *          The content.
	 * @param outputFile
	 *          The output file.
	 * @throws TransformerException
	 */
	public void prettifyAndPrintHtml(String content, File outputFile) throws TransformerException {
		// create the transformer
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();

		// set the output properties
		transformer.setOutputProperty(OutputKeys.METHOD, "html");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		// get the input source
		InputSource inputSource = new InputSource(new StringReader(content));

		// prettify and print
		transformer.transform(new SAXSource(inputSource), new StreamResult(outputFile));

	}
}