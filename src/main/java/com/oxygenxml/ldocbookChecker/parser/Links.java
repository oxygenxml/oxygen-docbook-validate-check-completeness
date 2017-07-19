package com.oxygenxml.ldocbookChecker.parser;

import java.util.Set;

public class Links {
	
	private Set<Link> externalLinks;
	private Set<Link> imgLinks;
	private Set<Link> internalLinks;

	
	public Links(Set<Link> externalLinks, Set<Link> imgLinks, Set<Link> internalLinks) {
		this.externalLinks = externalLinks;
		this.imgLinks = imgLinks;
		this.internalLinks = internalLinks;
	}


	public Set<Link> getExternalLinks() {
		return externalLinks;
	}


	public Set<Link> getImgLinks() {
		return imgLinks;
	}


	public Set<Link> getInternalLinks() {
		return internalLinks;
	}

	
	
}
