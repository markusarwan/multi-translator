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
import android.os.AsyncTask;
import android.util.TypedValue;
import android.widget.TextView;

import com.robtheis.aptr.language.Language;
import com.robtheis.aptr.translate.Translate;

/**
 * AsyncTask to request a machine translation from the web service and display the result in the
 * given TextView.
 * 
 * @author Robert Theis
 */
public final class TranslateApertiumAsyncTask extends AsyncTask<String, Void, Boolean> {
  private static final String API_KEY = "KMZfX32tsIPfivsByUexr84f08Y";

  private Language sourceLanguage;
  private Language targetLanguage;
  private String text;
  private String translatedText;
  private TextView textView;

  public TranslateApertiumAsyncTask(String text, Language sourceLanguage, Language targetLanguage, TextView textView) {
    this.sourceLanguage = sourceLanguage;
    this.targetLanguage = targetLanguage;
    this.text = text;
    this.textView = textView;
  }
  
  @Override
  protected synchronized void onPreExecute() {
    super.onPreExecute();
    
    textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL), Typeface.NORMAL);
    textView.setTextSize(14);
    textView.setText("Translating...");
  }
  
  @Override
  protected synchronized Boolean doInBackground(String... arg0) {
    Translate.setKey(API_KEY);
    try {
      // Request translation
      translatedText = Translate.execute(text, sourceLanguage, targetLanguage);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  protected synchronized void onPostExecute(Boolean result) {
    super.onPostExecute(result);
    
    if (result) {
      // Reset the text formatting
      if (textView != null) {
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL), Typeface.NORMAL);
      }
      
      // Put the translation into the textview
      textView.setText(translatedText.trim());

      // Crudely scale betweeen 22 and 32 -- bigger font for shorter text
      int scaledSize = Math.max(22, 32 - translatedText.length() / 4);
      textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);

    } else {
      textView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.ITALIC);
      textView.setText("Unavailable");
    }
  }
}
