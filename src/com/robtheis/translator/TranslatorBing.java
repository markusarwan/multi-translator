package com.robtheis.translator;

import android.util.Log;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

class TranslatorBing {
  private static final String TAG = TranslatorBing.class.getSimpleName();
  private static final String API_KEY = "FE55328FE94D3809B4C6F458F1C5E4E655FE47FF";
  
  private TranslatorBing() {  
    // Private constructor to enforce noninstantiability
  }

  // Translate using Microsoft Translate API
  static String translate(String sourceLanguageCode, String targetLanguageCode, String sourceText) {      
    Translate.setKey(API_KEY);
    try {
      return Translate.execute(sourceText, Language.fromString(sourceLanguageCode), 
          Language.fromString(targetLanguageCode));
    } catch (Exception e) {
      Log.e(TAG, "Caught exeption in translation request.");
      return Translator.BAD_TRANSLATION_MSG;
    }
  }
}