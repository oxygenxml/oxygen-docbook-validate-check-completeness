package com.oxygenxml.docbook.checker.parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Contains lists with found links, IDs and conditions .
 * 
 * @author intern4
 *
 */
public class DocumentDetails {
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
	 * List with all conditions from document.
	 */
	private LinkedHashMap<String, LinkedHashSet<String>> allConditions;
	
	/**
	 * External link multiplication in progress
	 */
	int externalLinksMultiplication = 10;

	
	/**
	 * Constructor
	 * @param externalLinks
	 * @param imgLinks
	 * @param paraIds
	 * @param internalLinks
	 */
	public DocumentDetails(List<Link> externalLinks, List<Link> imgLinks, List<Id> paraIds, List<Link> internalLinks) {
		this.externalLinks = externalLinks;
		this.imgLinks = imgLinks;
		this.paraIds = paraIds;
		this.internalLinks = internalLinks;
	}

	/**
	 * Constructor
	 */
	public DocumentDetails() {
		this.externalLinks = new ArrayList<Link>();
		this.imgLinks = new ArrayList<Link>();
		this.internalLinks = new ArrayList<Link>();
		this.paraIds = new ArrayList<Id>();
		this.allConditions = new LinkedHashMap<String, LinkedHashSet<String>>();
	}

	
	// Getters
	public LinkedHashMap<String, LinkedHashSet<String>> getAllConditions() {
		return allConditions;
	}

	public void setAllConditions(LinkedHashMap<String, LinkedHashSet<String>> allConditions) {
		this.allConditions = allConditions;
	}
	
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

	
	
	/**
	 * Add operation
	 * @param linkDetails
	 * @return
	 */
	public DocumentDetails add(DocumentDetails linkDetails) {
		
		this.externalLinks.addAll(linkDetails.externalLinks);
		this.internalLinks.addAll(linkDetails.internalLinks);
		this.imgLinks.addAll(linkDetails.imgLinks);
		this.paraIds.addAll(linkDetails.paraIds);
		this.allConditions.putAll(linkDetails.allConditions);

		return this;
	}

	
	/**
	 * Get the external links multiplication factor.
	 * @return
	 */
	private int getExternalLinksMultiplicationFactor(){
		int sum = internalLinks.size() + imgLinks.size();
		if(sum == 0){
			return 1;
		}
		else{
		return sum * externalLinksMultiplication;
		}
	}
	
	
	/**
	 * Get the size according to multiplication factor of external links.
	 * @return
	 */
	private float sizeWithMultiplication(){
		int toReturn = 0;

		toReturn += this.externalLinks.size() * getExternalLinksMultiplicationFactor();
		toReturn += this.internalLinks.size();
		toReturn += this.imgLinks.size();

		return toReturn;

	}
	
	/**
	 * Get the progress of a external link.
	 * @return
	 */
	public float getExternalProgress(){
		return getExternalLinksMultiplicationFactor()/sizeWithMultiplication();
	}
	
	/**
	 * Get the progress of a internal link.
	 * @return
	 */
	public float getInternalProgress(){
		return 1/sizeWithMultiplication();
	}
	
	/**
	 * Get the progress of a image.
	 * @return
	 */
	public float getImageProgress(){
		return 1/sizeWithMultiplication();
	}

	
}
