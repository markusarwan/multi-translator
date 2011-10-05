package com.robtheis.translator;

import android.util.Log;

import com.google.api.translate.Language;
import com.google.api.translate.Translate;

class TranslatorGoogle {
  private static final String TAG = TranslatorGoogle.class.getSimpleName();
  private static final String API_KEY = "AIzaSyC0Wd5wJPlLclmXsx09rnWh5UfCRbehtYE";

  private TranslatorGoogle() {  
    // Private constructor to enforce noninstantiability
  }

  // Translate using Google Translate API
  static String translate(String sourceLanguageCode, String targetLanguageCode, String sourceText) {   

    // Truncate excessively long strings. Limit for Google Translate is 5000 characters
    if (sourceText.length() > 4500) {
      sourceText = sourceText.substring(0, 4500);
    }
    
    Translate.setKey(API_KEY);
    Translate.setHttpReferrer("rmtheis.wordpress.com");
    try {
      return Translate.execute(sourceText, Language.fromString(sourceLanguageCode), 
          Language.fromString(targetLanguageCode));
    } catch (Exception e) {
      Log.e(TAG, "Caught exeption in translation request.");
      return Translator.BAD_TRANSLATION_MSG;
    }
  }
}