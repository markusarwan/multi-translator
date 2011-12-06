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
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.robtheis.aptr.language.Language;

/**
 * Place holder for the offline Apertium translator. Generates a pseudo-translation
 * for the given text, as a place holder for true translation that will be replaced
 * by a working system at a later date.
 * 
 * @author Robert Theis
 */
public final class TranslateOfflineAsyncTask extends AsyncTask<String, Void, Boolean> {

  private Language sourceLanguage;
  private Language targetLanguage;
  private String text;
  private String translatedText;
  private TextView textView;

  public TranslateOfflineAsyncTask(String text, Language sourceLanguage, Language targetLanguage, TextView textView) {
    this.sourceLanguage = sourceLanguage;
    this.targetLanguage = targetLanguage;
    this.text = text;
    this.textView = textView;
  }

  @Override
  protected synchronized void onPreExecute() {
    super.onPreExecute();
  }

  @Override
  protected synchronized Boolean doInBackground(String... arg0) {
    // Generate a pseudo-translation for the given text string.
    // No source/target language checking is performed.
    String[] words = text.split("\\s+");
    StringBuilder pseudoTranslation = new StringBuilder();
    for (String w : words) { 
      Log.d("TranslateOfflineAsyncTask", "word: " + w);
      try {
        pseudoTranslation.append(" ");
        // Check if the text matches our place holder values
        if (w.equalsIgnoreCase("stop")) {
          pseudoTranslation.append("alto");
        } else if (w.equalsIgnoreCase("alto")) {
          pseudoTranslation.append("stop");
        } else if (w.equalsIgnoreCase("hola")) {
          pseudoTranslation.append("hello");
        } else if (w.equalsIgnoreCase("hello")) {
          pseudoTranslation.append("hola");
        } else if (w.equalsIgnoreCase("prueba")) {
          pseudoTranslation.append("test");
        } else if (w.equalsIgnoreCase("test")) {
          pseudoTranslation.append("prueba");
        } else {
          pseudoTranslation.append(w);
        }
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
      translatedText = pseudoTranslation.toString();
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

      if (translatedText != null) {
        // Put the translation into the textview
        textView.setText(translatedText.trim());

        // Crudely scale betweeen 22 and 32 -- bigger font for shorter text
        int scaledSize = Math.max(22, 32 - translatedText.length() / 4);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);
      }
    } else {
      textView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.ITALIC);
      textView.setText("Unavailable");
    }
  }
}
