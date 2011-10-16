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

import android.graphics.Typeface;
import android.widget.TextView;

import com.google.api.translate.Language;

/**
 * Provides machine translations using the Google Translate API. 
 * 
 * @author Robert Theis
 */
class TranslatorGoogle {

  private TranslatorGoogle() {  
    // Private constructor to enforce noninstantiability
  }

  /**
   * Check if the supplied language pair is valid for this translation service, and start an 
   * AsyncTask to request the translation.
   * 
   * @param text The text to translate
   * @param source The source language for translation
   * @param target The target language for translation
   * @param textView The text field that will receive the completed translation
   */
  static void translate(String text, String source, String target, TextView textView) {      
    if (source == null || target == null) {
      throw new IllegalArgumentException();
    }

    Language sourceLanguage = null;
    Language targetLanguage = null;
    try {
      sourceLanguage = toLanguage(source);
      targetLanguage = toLanguage(target);
    } catch (IllegalArgumentException e) {
      textView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.ITALIC);
      textView.setTextSize(14);
      textView.setText("Unsupported language pair");
    }

    if (sourceLanguage != null && targetLanguage != null) {
      // Truncate excessively long strings. Limit for Google Translate is 5000 characters
      if (text.length() > 4500) {
        text = text.substring(0, 4500);
      }
      
      // Start an AsyncTask to perform the translation request.
      new TranslateGoogleAsyncTask(text, sourceLanguage, targetLanguage, textView).execute();
    } else {
      textView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.ITALIC);
      textView.setTextSize(14);
      textView.setText("Unsupported language pair");
    }
  }
  
  /**
   * Convert the given name of a natural language into a Language from the enum of Languages 
   * supported by this translation service.
   * 
   * @param languageName The name of the language, for example, "English"
   * @return The Language object representing this language
   * @throws IllegalArgumentException
   */
  private static Language toLanguage(String languageName) throws IllegalArgumentException {   
    // Convert string to all caps
    String standardizedName = languageName.toUpperCase();
    
    // Replace spaces with underscores
    standardizedName = standardizedName.replace(' ', '_');
    
    // Remove parentheses
    standardizedName = standardizedName.replace("(", "");   
    standardizedName = standardizedName.replace(")", "");
    
    // Hack to fix misspelling in google-api-translate-java
    if (standardizedName.equals("UKRAINIAN")) {
      standardizedName = "UKRANIAN";
    }
    
    // Nullify one of the languages in google-api-translate-java that's actually unsupported
    // by Google Translate. This is necessary because this language is included for Apertium,
    // so it's an allowed language on the merged list of languages for this project.
    if (standardizedName.equals("BASQUE")) {
      return null;
    }
    
    // Map Norwegian-Bokmal to Norwegian
    if (standardizedName.equals("NORWEGIAN_BOKMAL")) {
      standardizedName = "NORWEGIAN";
    }
    
    return Language.valueOf(standardizedName);
  }
}