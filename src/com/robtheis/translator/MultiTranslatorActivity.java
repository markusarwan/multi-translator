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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Android app to request machine translation from multiple web services.
 * 
 * Translations can be viewed alongside one another, providing a convenient way to compare output.
 * 
 * Offline translation using Apertium is provided as an additional service provider, acting as a 
 * test framework for porting Apertium to Android. 
 * 
 * @author Robert Theis
 */
public class MultiTranslatorActivity extends Activity {
  public static final String TAG = MultiTranslatorActivity.class.getSimpleName();

  private static EditText inputField;
  private Button goButton, swapButton;
  private View onlineTranslationsView;
  private static View section1, section2, section3, section4;
  private static TextView translation1, translation2, translation3, translation4;
  private static SharedPreferences sharedPreferences;
  private Spinner sourceSpinner, targetSpinner;

  private static final int SETTINGS_ID = Menu.FIRST;
  
  private static final int OPTIONS_COPY_TRANSLATION1 = Menu.FIRST;
  private static final int OPTIONS_COPY_TRANSLATION2 = Menu.FIRST + 1;
  private static final int OPTIONS_COPY_TRANSLATION3 = Menu.FIRST + 2;
  private static final int OPTIONS_COPY_TRANSLATION4 = Menu.FIRST + 3;

  private static String[] languagesApertiumOffline;
  private static String[] languagesApertiumOnline;
  private static String[] languagesMicrosoft;
  private static String[] languagesGoogle;
  private static String[] languages;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    swapButton = (Button) findViewById(R.id.button_swap_languages);
    goButton = (Button) findViewById(R.id.button_go);
    inputField = (EditText) findViewById(R.id.editText1);
    onlineTranslationsView = (View) findViewById(R.id.translations_online);
    section1 = (View) findViewById(R.id.trans_section1);
    section2 = (View) findViewById(R.id.trans_section2);
    section3 = (View) findViewById(R.id.trans_section3);
    section4 = (View) findViewById(R.id.trans_section4);
    translation1 = (TextView) findViewById(R.id.translation1);
    translation2 = (TextView) findViewById(R.id.translation2);
    translation3 = (TextView) findViewById(R.id.translation3);
    translation4 = (TextView) findViewById(R.id.translation4);
    
    // Accept long-press context menu events for translation text fields
    registerForContextMenu(translation1);
    registerForContextMenu(translation2);
    registerForContextMenu(translation3);
    registerForContextMenu(translation4);
    
    // Restore the translation text fields if this is an orientation change and we're restoring
    // the savedInstanceState.
    if (savedInstanceState != null && savedInstanceState.getBoolean("translations_view_visibility")) {
      onlineTranslationsView.setVisibility(View.VISIBLE);
      translation1.setTextSize(TypedValue.COMPLEX_UNIT_PX, savedInstanceState.getFloat("translation1_text_size"));
      translation2.setTextSize(TypedValue.COMPLEX_UNIT_PX, savedInstanceState.getFloat("translation2_text_size"));
      translation3.setTextSize(TypedValue.COMPLEX_UNIT_PX, savedInstanceState.getFloat("translation3_text_size"));
      translation4.setTextSize(TypedValue.COMPLEX_UNIT_PX, savedInstanceState.getFloat("translation4_text_size"));
      translation1.setText(savedInstanceState.getString("translation1_text"));
      translation2.setText(savedInstanceState.getString("translation2_text"));
      translation3.setText(savedInstanceState.getString("translation3_text"));
      translation4.setText(savedInstanceState.getString("translation4_text"));
    }
    if (savedInstanceState != null && savedInstanceState.getBoolean("section1_view_visibility")) {
      section1.setVisibility(View.VISIBLE);
    }
    
    // Set up the list of languages available, based on service preferences
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    languagesApertiumOffline = getResources().getStringArray(R.array.languages_apertium);
    languagesApertiumOnline = getResources().getStringArray(R.array.languages_apertium);
    languagesMicrosoft = getResources().getStringArray(R.array.languages_microsoft);
    languagesGoogle = getResources().getStringArray(R.array.languages_google);
    initLanguageList();

    goButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        startTranslation();
      }
    });
    
    swapButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        swapLanguages();
      }
    });

    inputField.addTextChangedListener(new TextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
        if (sharedPreferences.getBoolean(PreferencesActivity.KEY_TOGGLE_APERTIUM_OFFLINE, true)) {
          startOfflineTranslation();
        }
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Do nothing
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Do nothing
      }
    });
  }
  
  @Override
  public void onResume() {
    super.onResume();
    initLanguageList();
    initSpinners();
  }
  
  /**
   * Called before activity is killed for restoring state later. 
   * 
   * Save the visibility of the translations view, and the text and its formatting for completed 
   * translations.
   */
  @Override
  protected void onSaveInstanceState(Bundle state) {
    super.onSaveInstanceState(state);
    state.putBoolean("section1_view_visibility", section1.isShown());
    state.putBoolean("translations_view_visibility", onlineTranslationsView.isShown());
    state.putString("translation1_text", translation1.getText().toString());
    state.putString("translation2_text", translation2.getText().toString());
    state.putString("translation3_text", translation3.getText().toString());
    state.putString("translation4_text", translation4.getText().toString());
    // Save the text size. There was a problem saving the Typeface due to getTypeface() always
    // throwing a NullPointerException, so we do not save the Typeface.
    state.putFloat("translation1_text_size", translation1.getTextSize());
    state.putFloat("translation2_text_size", translation2.getTextSize());
    state.putFloat("translation3_text_size", translation3.getTextSize());
    state.putFloat("translation4_text_size", translation4.getTextSize());
  }
  
  /** Check service preferences, generate a list of available languages, and set TextView visibility. */
  public static void initLanguageList() {
    // If a particular service is active (checked), add its languages to languagesList
    boolean isApertiumOfflineEnabled = sharedPreferences.getBoolean(PreferencesActivity.KEY_TOGGLE_APERTIUM_OFFLINE, false);
    boolean isApertiumOnlineEnabled = sharedPreferences.getBoolean(PreferencesActivity.KEY_TOGGLE_APERTIUM_ONLINE, true);
    boolean isBingTranslatorEnabled = sharedPreferences.getBoolean(PreferencesActivity.KEY_TOGGLE_BING_TRANSLATOR, true);
    boolean isGoogleTranslateEnabled = sharedPreferences.getBoolean(PreferencesActivity.KEY_TOGGLE_GOOGLE_TRANSLATE, true);
    
    List<String[]> languagesList = new ArrayList<String[]>();
    
    // If all services are un-checked, enable Google Translate.
    if (!isApertiumOfflineEnabled && !isApertiumOnlineEnabled && !isBingTranslatorEnabled && !isGoogleTranslateEnabled) {
      sharedPreferences.edit().putBoolean(PreferencesActivity.KEY_TOGGLE_GOOGLE_TRANSLATE, true).commit();
      isGoogleTranslateEnabled = true;
    }
    
    // Set translation view visibility and build the list of languages available.
    if (isApertiumOfflineEnabled) {
      languagesList.add(languagesApertiumOffline);
    } else {
      section1.setVisibility(View.GONE);
      translation1.setText("");
    }
    if (isApertiumOnlineEnabled) {
      languagesList.add(languagesApertiumOnline);
      section2.setVisibility(View.VISIBLE);
    } else {
      section2.setVisibility(View.GONE);
      translation2.setText("");
    }
    if (isBingTranslatorEnabled) {
      languagesList.add(languagesMicrosoft);
      section3.setVisibility(View.VISIBLE);
    } else {
      section3.setVisibility(View.GONE);
      translation3.setText("");
    }
    if (isGoogleTranslateEnabled) {
      languagesList.add(languagesGoogle);
      section4.setVisibility(View.VISIBLE);
    } else {
      section4.setVisibility(View.GONE);
      translation4.setText("");
    }
    
    // Combine the various arrays of supported languages into a single list
    languages = combineLanguageLists(languagesList);
  }

  /** Set up the Spinners for selecting source/target languages from drop-down lists */
  public void initSpinners() {
    sourceSpinner = (Spinner) findViewById(R.id.spinner1);
    ArrayAdapter<String> sourceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
    sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    sourceSpinner.setAdapter(sourceAdapter);
    sourceSpinner.setOnItemSelectedListener(new SourceLanguageOnItemSelectedListener());

    targetSpinner = (Spinner) findViewById(R.id.spinner2);
    ArrayAdapter<String> targetAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
    targetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    targetSpinner.setAdapter(targetAdapter);
    targetSpinner.setOnItemSelectedListener(new TargetLanguageOnItemSelectedListener());
    
    // Set language selections on the spinners based on saved preferences
    String sourceLanguage = sharedPreferences.getString(PreferencesActivity.KEY_SOURCE_LANGUAGE_PREFERENCE, 
        PreferencesActivity.DEFAULT_SOURCE_LANGUAGE);
    String targetLanguage = sharedPreferences.getString(PreferencesActivity.KEY_TARGET_LANGUAGE_PREFERENCE, 
        PreferencesActivity.DEFAULT_TARGET_LANGUAGE);
    sourceSpinner.setSelection(sourceAdapter.getPosition(sourceLanguage));
    targetSpinner.setSelection(targetAdapter.getPosition(targetLanguage));
  }

  /** Swap the source language with the target language. */
  private void swapLanguages() {
    String sourceLanguage = sharedPreferences.getString(PreferencesActivity.KEY_SOURCE_LANGUAGE_PREFERENCE, 
        PreferencesActivity.DEFAULT_SOURCE_LANGUAGE);
    String targetLanguage = sharedPreferences.getString(PreferencesActivity.KEY_TARGET_LANGUAGE_PREFERENCE, 
        PreferencesActivity.DEFAULT_TARGET_LANGUAGE);
    
    // Save the current target language as the source language preference
    sharedPreferences.edit().putString(PreferencesActivity.KEY_SOURCE_LANGUAGE_PREFERENCE, 
        targetLanguage).commit();
    
    // Save the current source language as the target language preference
    sharedPreferences.edit().putString(PreferencesActivity.KEY_TARGET_LANGUAGE_PREFERENCE, 
        sourceLanguage).commit();
    
    // Set the prompt in the text input field
    inputField.setHint(getString(R.string.input_hint) + " " + targetLanguage);
    
    // Reset the currently-selected languages in the spinners
    initSpinners();
  }
  
  /**
   * Begin the translation on all available web services.
   */
  private void startTranslation() {   
    String source = sharedPreferences.getString(PreferencesActivity.KEY_SOURCE_LANGUAGE_PREFERENCE, 
        PreferencesActivity.DEFAULT_SOURCE_LANGUAGE);
    String target = sharedPreferences.getString(PreferencesActivity.KEY_TARGET_LANGUAGE_PREFERENCE, 
        PreferencesActivity.DEFAULT_TARGET_LANGUAGE);
    if (!source.equals(target)) {
      String text = inputField.getText().toString();
      if (text.length() > 0) {
        onlineTranslationsView.setVisibility(View.VISIBLE);
        MultiTranslator.translate(this, source, target, text);
      } else {
        // Tell the user to enter some text for translation
        Toast.makeText(getApplicationContext(), "Enter text for translation.", Toast.LENGTH_LONG);
      }
    } else {
      // Tell the user to choose a language pair
      Toast.makeText(getApplicationContext(), "Choose a language pair for translation", Toast.LENGTH_LONG);
    }
  }
  
  /**
   * Begin an offline translation.
   * 
   * @param textView The text field to receive the translated text
   */
  private void startOfflineTranslation() {
    String source = sharedPreferences.getString(PreferencesActivity.KEY_SOURCE_LANGUAGE_PREFERENCE, 
        PreferencesActivity.DEFAULT_SOURCE_LANGUAGE);
    String target = sharedPreferences.getString(PreferencesActivity.KEY_TARGET_LANGUAGE_PREFERENCE, 
        PreferencesActivity.DEFAULT_TARGET_LANGUAGE);
    
    // Hide the offline translation section if no text is present
    String text = inputField.getText().toString();
    if (text.length() == 0) {
      section1.setVisibility(View.GONE);
      onlineTranslationsView.setVisibility(View.GONE);
    } else if (!source.equals(target)) {
      section1.setVisibility(View.VISIBLE);
    	// Start the translation
    	TranslatorOffline.translateOffline(text, source, target, translation1);  
    } else {
      // Tell the user to choose a language pair
      Toast.makeText(getApplicationContext(), "Choose a language pair for translation", Toast.LENGTH_LONG);
    }
  }
  
  public class SourceLanguageOnItemSelectedListener implements OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
      String sourceLanguage = (String) sourceSpinner.getItemAtPosition(pos);
      
      // Save the source language preference
      sharedPreferences.edit().putString(PreferencesActivity.KEY_SOURCE_LANGUAGE_PREFERENCE, 
          sourceLanguage).commit();
      
      // Set the prompt in the text input field
      inputField.setHint(getString(R.string.input_hint) + " " + sourceLanguage);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
      // Do nothing
    }
  }

  public class TargetLanguageOnItemSelectedListener implements OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
      // Save the target language preference
      sharedPreferences.edit().putString(PreferencesActivity.KEY_TARGET_LANGUAGE_PREFERENCE, 
          (String) targetSpinner.getItemAtPosition(pos)).commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
      // Do nothing
    }
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v,
      ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    if (v.equals(translation1)) {
      menu.add(Menu.NONE, OPTIONS_COPY_TRANSLATION1, Menu.NONE, "Copy translated text");
    } else if (v.equals(translation2)) {
      menu.add(Menu.NONE, OPTIONS_COPY_TRANSLATION2, Menu.NONE, "Copy translated text");
    } else if (v.equals(translation3)) {
      menu.add(Menu.NONE, OPTIONS_COPY_TRANSLATION3, Menu.NONE, "Copy translated text");
    } else if (v.equals(translation4)) {
      menu.add(Menu.NONE, OPTIONS_COPY_TRANSLATION4, Menu.NONE, "Copy translated text");
    }
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    switch (item.getItemId()) {

    case OPTIONS_COPY_TRANSLATION1:
        clipboardManager.setText(translation1.getText());
      if (clipboardManager.hasText()) {
        Toast toast = Toast.makeText(this, "Text copied.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
      }
      return true;
    case OPTIONS_COPY_TRANSLATION2:
        clipboardManager.setText(translation2.getText());
      if (clipboardManager.hasText()) {
        Toast toast = Toast.makeText(this, "Text copied.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
      }
      return true;
    case OPTIONS_COPY_TRANSLATION3:
      clipboardManager.setText(translation3.getText());
    if (clipboardManager.hasText()) {
      Toast toast = Toast.makeText(this, "Text copied.", Toast.LENGTH_LONG);
      toast.setGravity(Gravity.BOTTOM, 0, 0);
      toast.show();
    }
    return true;
  case OPTIONS_COPY_TRANSLATION4:
      clipboardManager.setText(translation4.getText());
    if (clipboardManager.hasText()) {
      Toast toast = Toast.makeText(this, "Text copied.", Toast.LENGTH_LONG);
      toast.setGravity(Gravity.BOTTOM, 0, 0);
      toast.show();
    }
    return true;
    default:
      return super.onContextItemSelected(item);
    }
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    menu.add(0, SETTINGS_ID, 0, getString(R.string.menu_settings)).setIcon(android.R.drawable.ic_menu_preferences);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Intent intent;
    switch (item.getItemId()) {
      case SETTINGS_ID: {
        intent = new Intent().setClass(this, PreferencesActivity.class);
        startActivity(intent);
        break;
      }
    }
    return super.onOptionsItemSelected(item);
  }
  
  /**
   * Combine a List of String arrays into a single sorted string array with duplicates removed.
   * 
   * @param languagesList A list of all the services' individual language lists
   * @return A single alphabetized list with no duplicates
   */
  private static String[] combineLanguageLists(List<String[]> languagesList) {
    ArrayList<String> returnList = new ArrayList<String>(languagesList.size());
    // For each array of Strings in the List
    for (String[] input : languagesList) {
      // Add each string from this array of Strings
      for (int i = 0; i < input.length; i++) {
        returnList.add(input[i]);
      }
    }
    removeDuplicatesWithOrder(returnList);
    Collections.sort(returnList);
    return returnList.toArray(new String[returnList.size()]);
  }
  
  /**
   * Remove duplicate strings from an ArrayList of strings.
   * 
   * @param arrayList The arrayList with possible duplicate entries
   */
  private static void removeDuplicatesWithOrder(ArrayList<String> arrayList)
  {
    Set<String> set = new HashSet<String>();
    List<String> newList = new ArrayList<String>();
    for (Iterator<String> iter = arrayList.iterator(); iter.hasNext();) {
      Object element = iter.next();
      if (set.add((String) element))
        newList.add((String) element);
    }
    arrayList.clear();
    arrayList.addAll(newList);
  }
}
