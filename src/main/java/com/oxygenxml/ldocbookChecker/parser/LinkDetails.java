package com.oxygenxml.ldocbookChecker.parser;

import java.util.List;

/**
 * Contains list with categories of founded links.
 * @author intern4
 *
 */
public class LinkDetails {
	/**
	 * List with external links.
	 */
	private List<Link> externalLinks;
	/**
	 * List with images links.
	 */
	private List<Link> imgLinks;
	/**
	 * List with paragraph IDs.
	 */
	private List<Id> paraIds;
	/**
	 * List with internal links.
	 */
	private List<Link> internalLinks;
	
	/**
	 * List with xi-include links.
	 */
	private List<Link> includedDocumentsLinks;
	
	public LinkDetails(List<Link> includedDocumentsLinks, List<Link> externalLinks, List<Link> imgLinks, List<Id> paraIds, List<Link> internalLinks) {
		this.includedDocumentsLinks = includedDocumentsLinks;
		this.externalLinks = externalLinks;
		this.imgLinks = imgLinks;
		this.paraIds = paraIds;
		this.internalLinks = internalLinks;
	}


	// Getters
	public List<Link> getExternalLinks() {
		return externalLinks;
	}


	public List<Link> getImgLinks() {
		return imgLinks;
	}

	public List<Id> getParaIds() {
		return paraIds;
	}
	
	public List<Link> getInternalLinks() {
		return internalLinks;
	}

	public List<Link> getIncludedDocumentsLinks(){
		return includedDocumentsLinks;
	}
	
	/**
	 * Add all elements of given parameter to this.
	 * @param results 
	 * @return this
	 */
	public LinkDetails addAll(LinkDetails results){
		this.externalLinks.addAll( results.getExternalLinks() );
		this.internalLinks.addAll( results.getInternalLinks() );
		this.includedDocumentsLinks.addAll( results.getIncludedDocumentsLinks());
		this.paraIds.addAll( results.getParaIds());
		
		return this;
	}
	
}
