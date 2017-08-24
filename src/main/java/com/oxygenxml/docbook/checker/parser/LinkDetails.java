package com.oxygenxml.docbook.checker.parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Contains list with categories of founded links.
 * 
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

	private LinkedHashMap<String, LinkedHashSet<String>> allConditions;
	
	int externalLinksMultiplication = 10;

	public LinkDetails(List<Link> externalLinks, List<Link> imgLinks, List<Id> paraIds, List<Link> internalLinks) {
		this.externalLinks = externalLinks;
		this.imgLinks = imgLinks;
		this.paraIds = paraIds;
		this.internalLinks = internalLinks;
	}

	public LinkDetails() {
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

	public LinkDetails add(LinkDetails linkDetails) {

		this.externalLinks.addAll(linkDetails.externalLinks);
		this.internalLinks.addAll(linkDetails.internalLinks);
		this.imgLinks.addAll(linkDetails.imgLinks);
		this.paraIds.addAll(linkDetails.paraIds);
		this.allConditions.putAll(linkDetails.allConditions);

		return this;
	}

	/**
	 * Get the number of links.
	 * 
	 * @return
	 */
	public int size() {
		int toReturn = 0;

		toReturn += this.externalLinks.size();
		toReturn += this.internalLinks.size();
		toReturn += this.imgLinks.size();

		return toReturn;
	}
	
	
	private int getMultiplication(){
		int sum = internalLinks.size() + imgLinks.size();
		if(sum == 0){
			return 1;
		}
		else{
		return sum*10;
		}
	}
	
	private float sizeWithMultiplication(){
		int toReturn = 0;

		toReturn += this.externalLinks.size() * getMultiplication();
		toReturn += this.internalLinks.size();
		toReturn += this.imgLinks.size();

		return toReturn;

	}
	
	public float getExternalProgress(){
		return getMultiplication()/sizeWithMultiplication();
	}
	
	public float getInternalProgress(){
		return 1/sizeWithMultiplication();
	}
	
	public float getImageProgress(){
		return 1/sizeWithMultiplication();
	}

	
}
