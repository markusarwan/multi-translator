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

import com.robtheis.aptr.language.Language;

import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Place holder for offline Apertium translation being developed.
 * 
 * @author Robert Theis
 */
class TranslatorOffline extends TranslatorApertium {
  static void translateOffline(String text, String source, String target, TextView textView) {
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
	      new TranslateOfflineAsyncTask(text, sourceLanguage, targetLanguage, textView).execute();
	    } else {
	      // Languages are on the list, but this source/target pair is not supported
	      textView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.ITALIC);
	      textView.setTextSize(14);
	      textView.setText("Unsupported language pair");
	    }
  }
}