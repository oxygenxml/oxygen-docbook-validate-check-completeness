package com.oxygenxml.docbook.checker.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	 * Set with all conditions with details from document.
	 */
	private LinkedHashSet<ConditionDetails> allConditions;

	/**
	 * List with all assembly files and IDs. 
	 */
	private List<AssemblyFileId> assemblyFilesAndIds;
	
	/**
	 * List with assembly links.
	 */
	private List<Link> assemblyLinks;
	
	/**
	 * Constructor
	 * @param externalLinks External links list.
	 * @param imgLinks 	Images list.
	 * @param paraIds	Ids list.
	 * @param internalLinks Internal links list.
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
		this.allConditions = new LinkedHashSet<ConditionDetails>() ;
		this.assemblyFilesAndIds = new ArrayList<AssemblyFileId>();
		this.assemblyLinks = new ArrayList<Link>();
	}

	
	// Getters and setter
	public LinkedHashSet<ConditionDetails> getAllConditions() {
		return allConditions;
	}

	public void setAllConditions(LinkedHashSet<ConditionDetails> allConditions) {
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

	public List<AssemblyFileId> getAssemblyFilesAndIds() {
		return assemblyFilesAndIds;
	}

	public List<Link> getAssemblyLinks() {
		return assemblyLinks;
	}


	/**
	 * Add operation
	 * @param documentDetails 
	 * @return The results of add
	 */
	public DocumentDetails add(DocumentDetails documentDetails) {
		
		this.externalLinks.addAll(documentDetails.externalLinks);
		this.internalLinks.addAll(documentDetails.internalLinks);
		this.imgLinks.addAll(documentDetails.imgLinks);
		this.paraIds.addAll(documentDetails.paraIds);
		this.assemblyFilesAndIds.addAll(documentDetails.assemblyFilesAndIds);
		this.assemblyLinks.addAll(documentDetails.assemblyLinks);

		return this;
	}

	/**
	 * Add the given Id in paraId list.
	 * @param id The id.
	 */
	public void addId(Id id){
		paraIds.add(id);
	}
	
	/**
	 * Add the given link in externalLinks list.
	 * @param link The link.
	 */
	public void addExternalLink(Link link){
		externalLinks.add(link);
	}
	
	/**
	 * Add the given link in internalLinks list.
	 * @param link The link.
	 */
	public void addInternalLink(Link link){
		internalLinks.add(link);
	}
	
	/**
	 * Add the given link in imageLinks list.
	 * @param link The link.
	 */
	public void addImage(Link link){
		imgLinks.add(link);
	}
	
	/**
	 * Add the given assembly file in asssemblyFiles map.
	 * @param assemblyFileId The assembly file.
	 */
	public void addAssemblyFile(AssemblyFileId assemblyFileId){
		assemblyFilesAndIds.add(assemblyFileId);
	}
	
	/**
	 * Add the given link in assemblyLinks list.
	 * @param link The link.
	 */
	public void addAssemblyLink(Link link){
		assemblyLinks.add(link);
	}

	@Override
	public String toString() {
		return "DocumentDetails [externalLinks=" + externalLinks + ", imgLinks=" + imgLinks + ", paraIds=" + paraIds
				+ ", internalLinks=" + internalLinks + ", allConditions=" + allConditions + ", assemblyFilesAndIds="
				+ assemblyFilesAndIds + ", assemblyLinks=" + assemblyLinks + "]";
	}
	
	
	
	
}
