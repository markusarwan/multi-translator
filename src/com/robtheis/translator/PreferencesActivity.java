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

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;

/**
 * Class to handle preferences that are saved across sessions of the app.
 */
public class PreferencesActivity extends PreferenceActivity implements
  OnSharedPreferenceChangeListener {
  public static final String TAG = PreferencesActivity.class.getSimpleName();
  
  // Default languages
  public static final String DEFAULT_SOURCE_LANGUAGE = "English";
  public static final String DEFAULT_TARGET_LANGUAGE = "Spanish";
  
  // Language settings
  public static final String KEY_SOURCE_LANGUAGE_PREFERENCE = "preference_source_language";
  public static final String KEY_TARGET_LANGUAGE_PREFERENCE = "preference_target_language";
  public static final String KEY_SOURCE_LANGUAGE_DEFAULT = "preference_source_language_default";
  public static final String KEY_TARGET_LANGUAGE_DEFAULT = "preference_target_language_default";
  
  // Translation service settings
  public static final String KEY_TOGGLE_APERTIUM_OFFLINE = "preference_service1";
  public static final String KEY_TOGGLE_APERTIUM_ONLINE = "preference_service2";
  public static final String KEY_TOGGLE_BING_TRANSLATOR = "preference_service3";
  public static final String KEY_TOGGLE_GOOGLE_TRANSLATE = "preference_service4";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.preferences);
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    
    // Disable offline translation option in preparation for Android Market release
//    CheckBoxPreference offlineCheckBoxPref = (CheckBoxPreference) findPreference(KEY_TOGGLE_APERTIUM_OFFLINE);
//    offlineCheckBoxPref.setEnabled(false);
//    offlineCheckBoxPref.setSummary("Coming soon");
  }

  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {   
    // Do nothing
  }
}