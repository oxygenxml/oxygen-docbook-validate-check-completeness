package tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.oxygenxml.docbook.checker.parser.ParserCreator;

/**
 * Parser creator used in JUnits.
 * @author Cosmin Duna
 *
 */
public class PlainParserCreator implements ParserCreator {

	@Override
	public XMLReader createXMLReader() throws ParserConfigurationException, SAXException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setFeature("http://apache.org/xml/features/xinclude", true);
		
		SAXParser saxParser = factory.newSAXParser();
		XMLReader reader = saxParser.getXMLReader();
		reader.setEntityResolver(new EntityResolver() {
			
			@Override
			public InputSource resolveEntity(String publicId, String systemId)
					throws SAXException, IOException {
				if(publicId != null){
					//Resolve Docbook 4 DTD Public IDs
					return new InputSource(new ByteArrayInputStream(new byte[0]));
				} else {
					return null;
				}
			}
		});
		return reader;
	}

}
 