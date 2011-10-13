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
import android.widget.TextView;

/**
 * Delegates online translation requests to the appropriate translator class. Translations are 
 * requested for every machine translation web service enabled in the SharedPreferences.
 * 
 * @author Robert Theis
 */
public class MultiTranslator {    
  private static TextView translation2, translation3, translation4;
  
  private MultiTranslator(Activity activity) {  
    // Private constructor to enforce noninstantiability
  }
  
  /**
   * Translate the given phrase using all selected web services.
   * 
   * @param activity The Activity requesting the translation
   * @param sourceLanguage The source language for translation
   * @param targetLanguage The target language for translation
   * @param text The text to translate
   */
  static void translate(Activity activity, String sourceLanguage, String targetLanguage, String text) {   
    // For each active (checked) web service in preferences, request a translation
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
    if (sharedPreferences.getBoolean(PreferencesActivity.KEY_TOGGLE_APERTIUM_ONLINE, true)) {
      translation2 = (TextView) activity.findViewById(R.id.translation2);
      TranslatorApertium.translate(text, sourceLanguage, targetLanguage, translation2);
    }
    if (sharedPreferences.getBoolean(PreferencesActivity.KEY_TOGGLE_BING_TRANSLATOR, true)) {
      translation3 = (TextView) activity.findViewById(R.id.translation3);
      TranslatorBing.translate(text, sourceLanguage, targetLanguage, translation3);
    }
    if (sharedPreferences.getBoolean(PreferencesActivity.KEY_TOGGLE_GOOGLE_TRANSLATE, true)) {
      translation4 = (TextView) activity.findViewById(R.id.translation4);
      TranslatorGoogle.translate(text, sourceLanguage, targetLanguage, translation4);
    }
  }
}