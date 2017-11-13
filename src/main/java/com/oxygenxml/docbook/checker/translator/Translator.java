package com.oxygenxml.docbook.checker.translator;

/**
 * Interface used for internationalization.
 * @author Cosmin Duna
 *
 */
public interface Translator {
	
	/**
	 * Get the translation from the given key;
	 * @param key the key.
	 * @return the translation.
	 */
	 String getTranslation(String key);
}
