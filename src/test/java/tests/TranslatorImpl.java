package tests;

import com.oxygenxml.docbook.checker.translator.Translator;

public class TranslatorImpl implements Translator {

	@Override
	public String getTranslation(String key) {
		return "translation";
	}

}
