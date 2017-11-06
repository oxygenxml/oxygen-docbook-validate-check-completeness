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

import org.apache.log4j.Logger;
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
	private static final String FIRST_PART = "<!DOCTYPE html>\r\n" + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n"
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
	private static final String FINAL_PART = "               </ul>\r\n" + "            </div>\r\n" + "        </div>\r\n"
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
	private static final String FILE_ICON = "data-jstree='{\"icon\":\"glyphicon glyphicon-file\"}'";
	/**
	 * The link icon
	 */
	private static final String LINK_ICON = "data-jstree='{\"icon\":\"glyphicon glyphicon-link\"}'";

	/**
	 * XMLUtilAccess
	 */
	private final XMLUtilAccess xmlUtilAccess = PluginWorkspaceProvider.getPluginWorkspace().getXMLUtilAccess();

	/**
	 * Logger
	 */
	 private static final Logger logger = Logger.getLogger(HtmlReportCreator.class);
	
	/**
	 * Convert the given tree in HTML.
	 * 
	 * @param root
	 *          The root of tree.
	 * @return The HTML content in String format.
	 */
	public String convertToHtml(DefaultMutableTreeNode root, File outputFile) {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(FIRST_PART);
		stringBuilder.append(convertToHtmlTree(root, outputFile));
		stringBuilder.append(FINAL_PART);
		
		return stringBuilder.toString();
	}

	private String convertToHtmlTree(DefaultMutableTreeNode root, File outputFile) {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append( "<li ");

		Object rootObje = root.getUserObject();
		String text;
		String anchor = "";

		// if the node is a Link
		if (rootObje instanceof Link) {
			Link link = (Link) rootObje;
			text = link.getRef();

			// get the anchor href
			if (link.getLinkType() != LinkType.INTERNAL) {
				URL absolutLocation = link.getAbsoluteLocation();
				if (absolutLocation != null) {
					try {
						anchor = absolutLocation.toURI().toString();
					}catch (URISyntaxException e) {
						logger.debug(e.getMessage(), e);
					}
				}else {
					anchor = link.getRef();
				}

			} else {
				try {
					anchor = link.getDocumentURL().toURI().toString();
				} catch (URISyntaxException e) {
					logger.debug(e.getMessage(), e);
				}
			}

			// relativize the path
			if (link.getLinkType() != LinkType.EXTERNAL) {
				try {
					anchor = Paths.get(outputFile.toURI()).relativize(Paths.get(anchor)).toString();
				} catch (IllegalArgumentException e) {
					logger.debug(e.getMessage(), e);
				}
			}
			stringBuilder.append(LINK_ICON + " >" + "<a href=\"" + xmlUtilAccess.escapeAttributeValue(anchor) + "\">"
					+ xmlUtilAccess.escapeTextValue(text) + "</a>");
			
		} else if (rootObje instanceof HierarchyReportStorageTreeNodeId) {
			// if the node is a HierarchyReportStorageTreeNodeId (root node with a
			// condition set)
			HierarchyReportStorageTreeNodeId nodeId = (HierarchyReportStorageTreeNodeId) rootObje;
			anchor = nodeId.getDocumentUrl().toString();
			text = anchor.substring(anchor.lastIndexOf('/') + 1) + " - " + nodeId.getConditionSet();

			try {
				anchor = Paths.get(outputFile.toURI()).relativize(Paths.get(nodeId.getDocumentUrl().toURI())).toString();
			} catch (IllegalArgumentException e) {
				logger.debug(e.getMessage(), e);
			} catch (URISyntaxException e) {
				logger.debug(e.getMessage(), e);
			}
			stringBuilder.append(FILE_ICON + " >" + "<a href=\"" + xmlUtilAccess.escapeAttributeValue(anchor) + "\">"
					+ xmlUtilAccess.escapeTextValue(text) + "</a>");
			
		} else if (rootObje instanceof URL) {
			// if the node is a URL
			URL url = (URL) rootObje;
			anchor = url.toString();
			text = anchor.substring(anchor.lastIndexOf('/') + 1);
			try {
				anchor = Paths.get(outputFile.toURI()).relativize(Paths.get(url.toURI())).toString();
			}catch (IllegalArgumentException e) {
				logger.debug(e.getMessage(), e);
			}catch (URISyntaxException e) {
				logger.debug(e.getMessage(), e);
			}

			stringBuilder.append(FILE_ICON + " >" + "<a href=\"" + xmlUtilAccess.escapeAttributeValue(anchor) + "\">"
					+ xmlUtilAccess.escapeTextValue(text) + "</a>");
		} else {
			text = (String) rootObje;
			stringBuilder.append( " > " + text);
		}

		int childCount = root.getChildCount();
		if (childCount > 0) {
			stringBuilder.append("<ul>");
		}

		for (int i = 0; i < childCount; i++) {
			stringBuilder.append( convertToHtmlTree((DefaultMutableTreeNode) root.getChildAt(i), outputFile));
		}

		if (childCount > 0) {
			stringBuilder.append("</ul>");
		}

		stringBuilder.append("</li>");
		
		return stringBuilder.toString();
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
