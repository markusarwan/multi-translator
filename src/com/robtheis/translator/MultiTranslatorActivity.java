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
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.robtheis.aptr.language.Language;
import com.robtheis.aptr.translate.Translate;


public class MultiTranslatorActivity extends Activity {
    
    EditText inputField;
    Button goButton;
    TextView translationApertiumOffline, translationApertiumOnline, translationBing, translationGoogle;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //label1 = (TextView) findViewById(R.id.text1);
        
        // Set the source/target language drop-downs based on saved preferences
        Spinner sourceSpinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> sourceAdapter = ArrayAdapter.createFromResource(
                this, R.array.languages, android.R.layout.simple_spinner_item);
        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceSpinner.setAdapter(sourceAdapter);
        
        Spinner targetSpinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> targetAdapter = ArrayAdapter.createFromResource(
                this, R.array.languages, android.R.layout.simple_spinner_item);
        targetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        targetSpinner.setAdapter(targetAdapter);
        
        
        // Set the visibility of translation text fields based on the current preferences
        
        
        
//        // APERTIUM
//        
//        Translate.setKey("");
//	try {
//	    String translatedText = Translate.execute("The quick brown fox jumps over the lazy dog.", Language.ENGLISH, Language.SPANISH);
//	    System.out.println(translatedText);
//	    textView.setText(translatedText);
//	    
//	} catch (Exception e) {
//	    e.printStackTrace();
//	}
//
//	// MICROSOFT
//	com.memetix.mst.translate.Translate.setKey("");
//	try {
//	    String translatedText = 
//		com.memetix.mst.translate.Translate.execute("The quick brown fox jumps over the lazy dog.", com.memetix.mst.language.Language.ENGLISH, 
//			com.memetix.mst.language.Language.SPANISH);
//	    System.out.println(translatedText);
//	    textView.append("\n" + translatedText);
//	    
//	} catch (Exception e) {
//	    e.printStackTrace();
//	}
	
	// Get the translation asynchronously
	//new TranslateAsyncTask(this, translationTextView, progressView, translationLanguageTextView,
	//          sourceLanguageCodeTranslation, targetLanguageCodeTranslation, ocrResult.getText()).execute();
	
	
        
        
        
    }
    
    
}