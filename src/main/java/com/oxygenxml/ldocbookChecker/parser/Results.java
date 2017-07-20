package com.oxygenxml.ldocbookChecker.parser;

import java.util.List;

/**
 * Contains list with categories of founded links.
 * @author intern4
 *
 */
public class Results {
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
	
	public Results(List<Link> includedDocumentsLinks, List<Link> externalLinks, List<Link> imgLinks, List<Id> paraIds, List<Link> internalLinks) {
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
	
	
}
