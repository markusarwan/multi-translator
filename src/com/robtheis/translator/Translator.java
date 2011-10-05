/*
 * Copyright (C) 2011 Robert Theis
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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