package com.robtheis.translator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Translator {

  public enum Service {
    APERTIUM_HTTP,
    APERTIUM_OFFLINE,
    BING_TRANSLATOR,
    GOOGLE_TRANSLATE
  };
    
  public static final String BAD_TRANSLATION_MSG = "[Translation unavailable]";
  
  private Translator(Activity activity) {  
    // Private constructor to enforce noninstantiability
  }
  
  static String translate(Activity activity, String sourceLanguageCode, String targetLanguageCode, String sourceText) {   
    
//    // Check preferences to determine which translation API to use--Google, or Bing.
//    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
//    String api = prefs.getString(PreferencesActivity.KEY_TRANSLATOR, CaptureActivity.DEFAULT_TRANSLATOR);
//    
//    if (sourceLanguageCode.equals(targetLanguageCode)) {
//      sourceLanguageCode = "";
//    }
//    
//    // Delegate the translation based on the user's preference.
//    if (api.equals(PreferencesActivity.TRANSLATOR_BING)) {
//      return TranslatorBing.translate(sourceLanguageCode, targetLanguageCode, sourceText);
//    } else if (api.equals(PreferencesActivity.TRANSLATOR_GOOGLE)) {
//      return TranslatorGoogle.translate(sourceLanguageCode, targetLanguageCode, sourceText);
//    }
    return BAD_TRANSLATION_MSG;
  }
}