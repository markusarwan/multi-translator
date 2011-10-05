package com.robtheis.translator;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public final class TranslateAsyncTask extends AsyncTask<String, String, Boolean> {

  private static final String TAG = TranslateAsyncTask.class.getSimpleName();

  private TextView textView;
  private View progressView;
  private TextView targetLanguageTextView;
  private String sourceLanguageCode;
  private String targetLanguageCode;
  private String sourceText;
  private String translatedText = "";

  public TranslateAsyncTask(TextView textView, View progressView, 
      TextView targetLanguageTextView, String sourceLanguageCode, String targetLanguageCode, String sourceText) {
    this.textView = textView;
    this.progressView = progressView;
    this.targetLanguageTextView = targetLanguageTextView;
    this.sourceLanguageCode = sourceLanguageCode;
    this.targetLanguageCode = targetLanguageCode;
    this.sourceText = sourceText;
  }
  
  @Override
  protected synchronized Boolean doInBackground(String... arg0) { // TODO is synchronized OK here?
//    translatedText = Translator.translate(sourceLanguageCode, targetLanguageCode, sourceText);

    // Check for failed translations.
    if (translatedText.equals(Translator.BAD_TRANSLATION_MSG)) {
      return false;
    }
    
    return true;
  }

  @Override
  protected synchronized void onPostExecute(Boolean result) {
    super.onPostExecute(result);
    
    if (result) {
      //Log.i(TAG, "SUCCESS");
      if (targetLanguageTextView != null) {
        targetLanguageTextView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL), Typeface.NORMAL);
      }
      textView.setText(translatedText);
      textView.setVisibility(View.VISIBLE);
//      textView.setTextColor(activity.getResources().getColor(R.color.translation_text));

      // Crudely scale betweeen 22 and 32 -- bigger font for shorter text
      int scaledSize = Math.max(22, 32 - translatedText.length() / 4);
      textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);

    } else {
      Log.e(TAG, "FAILURE");
      targetLanguageTextView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.ITALIC);
      targetLanguageTextView.setText("Unavailable");

    }
    
    // Turn off the indeterminate progress indicator
    if (progressView != null) {
      progressView.setVisibility(View.GONE);
    }
  }
}
