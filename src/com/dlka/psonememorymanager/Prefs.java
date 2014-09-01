package com.dlka.psonememorymanager;

import java.util.Map;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Prefs extends PreferenceActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);            
                        
            setResult(RESULT_OK);
            
    }
	
	@Override
	protected void onPause(){
		Map<String, ?> preferences;
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	preferences = prefs.getAll();
    	Boolean backup = (Boolean) preferences.get("backupPref");
    	if(backup==null||backup==true){
    		Statics.backup = true;
    	}
    	else
    		Statics.backup = false;
    	
    	Boolean export = (Boolean) preferences.get("exportPref");
    	if(export==null||export==true){
    		Statics.export = true;
    	}
    	else
    		Statics.export = false;
    	String format = (String) preferences.get("formatPref");
    	if(format!=null){
    		Statics.exportFmt = Integer.parseInt(format);
    	}
    	else
    		Statics.exportFmt = 0;
    	String about_text = (String) preferences.get("aboutPref");
    	if (about_text !=null){
    		
    	}
    	/*	WebView myWebView = (WebView) findViewById(R.id.webview);
		    myWebView.setWebViewClient(new WebViewClient());
    		myWebView.loadUrl("http://github.com/TopherLee513/PSone-Memory-Manager");*/
    	String exit = (String) preferences.get("exitPref");
    	if (exit !=null){
    		
    	}
		super.onPause();
	}
	

}
