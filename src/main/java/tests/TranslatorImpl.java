package tests;

import com.oxygenxml.docbookChecker.translator.Translator;

public class TranslatorImpl implements Translator {

	@Override
	public String getTranslation(String key) {
		return "translation";
	}

}
