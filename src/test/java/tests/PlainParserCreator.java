package tests;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.oxygenxml.docbook.checker.parser.ParserCreator;

/**
 * Parser creator used in JUnits.
 * @author intern4
 *
 */
public class PlainParserCreator implements ParserCreator {

	@Override
	public XMLReader createXMLReader() throws ParserConfigurationException, SAXException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);

		factory.setFeature("http://apache.org/xml/features/xinclude", true);
		
		SAXParser saxParser = factory.newSAXParser();
		
		return saxParser.getXMLReader();
	}

}
 