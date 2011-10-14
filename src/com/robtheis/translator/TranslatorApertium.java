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

import com.robtheis.aptr.language.Language;

/**
 * Provides machine translations using the Apertium web service API. 
 * 
 * @author Robert Theis
 */
class TranslatorApertium {

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
      
      // The source or target language is not in the enum list of Languages for this service.
      textView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.ITALIC);
      textView.setTextSize(14);
      textView.setText("Unsupported language pair");
    }
    
    // Check if this is a valid language pair for this web service
    if (isValidLanguagePair(sourceLanguage, targetLanguage)) {     
      // Start an AsyncTask to perform the translation request.
      new TranslateApertiumAsyncTask(text, sourceLanguage, targetLanguage, textView).execute();
    } else {
      // Languages are on the list, but this source/target pair is not supported
      textView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.ITALIC);
      textView.setTextSize(14);
      textView.setText("Unsupported language pair");
    }
  }
  
  /**
   * Check whether the given source language/target language pair is supported by this 
   * translation service. 
   * 
   * This is a separate check from checking whether the languages are listed in the enum of
   * Languages supported by this service. For example, Apertium supports both French and
   * English as languages, but French-to-English and English-to-French are not supported for 
   * translation.
   * 
   * @param source The source language for translation
   * @param target The target language for translation
   * @return True if the given language pair is valid
   */
  protected static boolean isValidLanguagePair(Language sourceLanguage, Language targetLanguage) {
    if (sourceLanguage == null || targetLanguage == null) {
      return false;
    }
    
    // Check if we have a "stable" and released language pair. 
    // Source: wiki.apertium.org/wiki/Main_Page
    if ( (sourceLanguage.equals(Language.ARAGONESE) && targetLanguage.equals(Language.SPANISH)) || 
         (sourceLanguage.equals(Language.ASTURIAN) && targetLanguage.equals(Language.SPANISH)) ||
         (sourceLanguage.equals(Language.BASQUE) && targetLanguage.equals(Language.SPANISH)) ||
         (sourceLanguage.equals(Language.BRETON) && targetLanguage.equals(Language.FRENCH)) ||
         (sourceLanguage.equals(Language.BULGARIAN) && targetLanguage.equals(Language.MACEDONIAN)) ||
         (sourceLanguage.equals(Language.CATALAN) && targetLanguage.equals(Language.ENGLISH)) ||
         (sourceLanguage.equals(Language.CATALAN) && targetLanguage.equals(Language.FRENCH)) ||
         (sourceLanguage.equals(Language.CATALAN) && targetLanguage.equals(Language.OCCITAN)) ||
         (sourceLanguage.equals(Language.CATALAN) && targetLanguage.equals(Language.PORTUGUESE)) ||
         (sourceLanguage.equals(Language.CATALAN) && targetLanguage.equals(Language.SPANISH)) ||
         (sourceLanguage.equals(Language.ENGLISH) && targetLanguage.equals(Language.CATALAN)) ||
         (sourceLanguage.equals(Language.ENGLISH) && targetLanguage.equals(Language.GALICIAN)) ||
         (sourceLanguage.equals(Language.ENGLISH) && targetLanguage.equals(Language.SPANISH)) ||
         (sourceLanguage.equals(Language.FRENCH) && targetLanguage.equals(Language.CATALAN)) ||
         (sourceLanguage.equals(Language.FRENCH) && targetLanguage.equals(Language.SPANISH)) ||
         (sourceLanguage.equals(Language.GALICIAN) && targetLanguage.equals(Language.ENGLISH)) ||
         (sourceLanguage.equals(Language.GALICIAN) && targetLanguage.equals(Language.PORTUGUESE)) ||
         (sourceLanguage.equals(Language.GALICIAN) && targetLanguage.equals(Language.SPANISH)) ||
         (sourceLanguage.equals(Language.ICELANDIC) && targetLanguage.equals(Language.ENGLISH)) ||
         (sourceLanguage.equals(Language.ITALIAN) && targetLanguage.equals(Language.CATALAN)) ||
         (sourceLanguage.equals(Language.MACEDONIAN) && targetLanguage.equals(Language.BULGARIAN)) ||
         (sourceLanguage.equals(Language.MACEDONIAN) && targetLanguage.equals(Language.ENGLISH)) ||
         (sourceLanguage.equals(Language.NORWEGIAN_BOKMAL) && targetLanguage.equals(Language.NORWEGIAN_NYNORSK)) ||
         (sourceLanguage.equals(Language.NORWEGIAN_NYNORSK) && targetLanguage.equals(Language.NORWEGIAN_BOKMAL)) ||
         (sourceLanguage.equals(Language.OCCITAN) && targetLanguage.equals(Language.CATALAN)) ||
         (sourceLanguage.equals(Language.OCCITAN) && targetLanguage.equals(Language.SPANISH)) ||
         (sourceLanguage.equals(Language.PORTUGUESE) && targetLanguage.equals(Language.CATALAN)) ||
         (sourceLanguage.equals(Language.PORTUGUESE) && targetLanguage.equals(Language.GALICIAN)) ||
         (sourceLanguage.equals(Language.PORTUGUESE) && targetLanguage.equals(Language.SPANISH)) ||
         (sourceLanguage.equals(Language.ROMANIAN) && targetLanguage.equals(Language.SPANISH)) ||
         (sourceLanguage.equals(Language.SPANISH) && targetLanguage.equals(Language.ARAGONESE)) ||
         (sourceLanguage.equals(Language.SPANISH) && targetLanguage.equals(Language.ASTURIAN)) ||
         (sourceLanguage.equals(Language.SPANISH) && targetLanguage.equals(Language.CATALAN)) ||
         (sourceLanguage.equals(Language.SPANISH) && targetLanguage.equals(Language.ENGLISH)) ||
         (sourceLanguage.equals(Language.SPANISH) && targetLanguage.equals(Language.FRENCH)) ||
         (sourceLanguage.equals(Language.SPANISH) && targetLanguage.equals(Language.GALICIAN)) ||
         (sourceLanguage.equals(Language.SPANISH) && targetLanguage.equals(Language.OCCITAN)) ||
         (sourceLanguage.equals(Language.SPANISH) && targetLanguage.equals(Language.PORTUGUESE)) ||
         (sourceLanguage.equals(Language.SWEDISH) && targetLanguage.equals(Language.DANISH)) ||
         (sourceLanguage.equals(Language.WELSH) && targetLanguage.equals(Language.ENGLISH)) ) {
      return true;
    }
    return false;
  }
  
  /**
   * Convert the given name of a natural language into a Language from the enum of Languages 
   * supported by this translation service.
   * 
   * @param languageName The name of the language, for example, "English"
   * @return The Language object representing this language
   * @throws IllegalArgumentException
   */
  protected static Language toLanguage(String languageName) throws IllegalArgumentException {   
    // Convert string to all caps
    String standardizedName = languageName.toUpperCase();
    
    // Replace spaces with underscores
    standardizedName = standardizedName.replace(' ', '_');
    
    // Remove parentheses
    standardizedName = standardizedName.replace("(", "");   
    standardizedName = standardizedName.replace(")", "");
    
    // Map Norwegian to Norwegian-Bokmal
    if (standardizedName.equals("NORWEGIAN")) {
      standardizedName = "NORWEGIAN_BOKMAL";
    }
    
    return Language.valueOf(standardizedName);
  }
}