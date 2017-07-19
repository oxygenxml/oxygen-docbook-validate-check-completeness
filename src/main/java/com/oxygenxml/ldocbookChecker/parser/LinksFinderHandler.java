package com.oxygenxml.ldocbookChecker.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.LocatorImpl;


/**
 * Handler for find externalLink
 * @author intern4
 *
 */
public class LinksFinderHandler extends DefaultHandler {
	
	/**
	 * Set of external founded links
	 */
	private Set<Link> externalLinksSet = new LinkedHashSet<Link>();
	
	/**
	 * Set of internal founded links
	 */
	private Set<Link> internalLinksSet = new LinkedHashSet<Link>();

	/**
	 * Set of links of image founded
	 */
	private Set<Link> imgLinksSet = new LinkedHashSet<Link>();
	
  private Locator locator = new LocatorImpl();
  
  /**
	 * The URL of the parsed document.
	 */
  private URL parentUrl; 
  
  /**
   * Constructor
   * @param url the parsed url
   */
  public LinksFinderHandler(URL url) {
  	parentUrl = url;
  }

  public void setDocumentLocator(Locator locator) {
    this.locator = locator;
  }
  
	@Override
	public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes)
			throws SAXException {
		super.startElement(uri, localName, qName, attributes);

		findExternalLink(qName, attributes);
		
		findImgLink(qName, attributes);
		
	

	}
	
	
	/**
	 * Find external link
	 * @param qName element 
	 * @param attributes  attributes
	 */
	public void findExternalLink(String qName, org.xml.sax.Attributes attributes){
		// db5
		if (qName.compareTo("link") == 0) {
			String atributeVal = attributes.getValue("xlink:href");
			
			if( atributeVal != null ){
				//add new Link in linksSet
				externalLinksSet.add(new Link(atributeVal , parentUrl, locator.getLineNumber(), locator.getColumnNumber()));
			}
		}

		//db4
		if (qName.compareTo("ulink") == 0) {
			String atributeVal = attributes.getValue("url");

			if( atributeVal != null ){
				//add new Link in linksSet
				externalLinksSet.add(new Link(atributeVal, parentUrl, locator.getLineNumber(), locator.getColumnNumber()));
			}
		}
	}

	
	/**
	 * Find image link
	 * @param qName element 
	 * @param attributes  attributes
	 */
	public void findImgLink(String qName, org.xml.sax.Attributes attributes){
		// db5
		if (qName.compareTo("imagedata") == 0) {
			String atributeVal = attributes.getValue("fileref");
			
			if( atributeVal != null ){
				//add new Link in linksSet
				imgLinksSet.add(new Link(atributeVal , parentUrl, locator.getLineNumber(), locator.getColumnNumber()));
			}
		}

		//db4
		if (qName.compareTo("inlinegraphic") == 0 || qName.compareTo("graphic") == 0 ) {
			String atributeVal = attributes.getValue("fileref");

			if( atributeVal != null ){
				//add new Link in linksSet
				imgLinksSet.add(new Link(atributeVal, parentUrl, locator.getLineNumber(), locator.getColumnNumber()));
			}
		}
	}
	
	
	
	/**
	 * Get founded links.
	 * @return the set with founded links.
	 */
	public Links getLinks(){
		return new Links(externalLinksSet, imgLinksSet, internalLinksSet);
	}

	
	
	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
		return new InputSource(new ByteArrayInputStream(new byte[0]));
	}

	
}
