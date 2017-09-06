package com.oxygenxml.docbook.checker.validator;

import com.oxygenxml.docbook.checker.parser.DocumentDetails;

/**
 * Class used for determinate the progress report of links.
 * @author intern4
 *
 */
public class ProgressDeterminator {
	
	/**
	 * The document details.
	 */
	private DocumentDetails documentDetails;
	
	/**
	 * External link multiplication in progress
	 */
	int externalLinksMultiplication = 10;
	
	/**
	 * Constructor.
	 * @param documentDetails The document details.
	 */
	public ProgressDeterminator(DocumentDetails documentDetails) {
		this.documentDetails = documentDetails;
	}
	

	/**
	 * Get the external links multiplication factor.
	 * @return The external links multiplication factor.
	 */
	private int getExternalLinksMultiplicationFactor(){
		int sum = documentDetails.getInternalLinks().size() + documentDetails.getImgLinks().size();
		if(sum == 0){
			return 1;
		}
		else{
		return sum * externalLinksMultiplication;
		}
	}
	
	
	/**
	 * Get the size according to multiplication factor of external links.
	 * @return The size.
	 */
	private float sizeWithMultiplication(){
		int toReturn = 0;

		toReturn += documentDetails.getExternalLinks().size() * getExternalLinksMultiplicationFactor();
		toReturn += documentDetails.getInternalLinks().size();
		toReturn += documentDetails.getInternalLinks().size();

		return toReturn;

	}
	
	/**
	 * Get the progress of a external link.
	 * @return The progress.
	 */
	public float getExternalProgress(){
		return getExternalLinksMultiplicationFactor()/sizeWithMultiplication();
	}
	
	/**
	 * Get the progress of a internal link.
	 * @return The progress.
	 */
	public float getInternalProgress(){
		return 1/sizeWithMultiplication();
	}
	
	/**
	 * Get the progress of a image.
	 * @return The progress.
	 */
	public float getImageProgress(){
		return 1/sizeWithMultiplication();
	}

	
}
