package com.topherlee.psonememorymanager;

import java.util.Map;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.webkit.WebView;

import com.topherlee.psonememorymanager.R;

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
    	Boolean about = (Boolean) preferences.get("aboutPref");
    	if(about==null||about==true){
    		Statics.about = true;
    		WebView myWebView = (WebView) findViewById(R.id.webview);
    		myWebView.loadUrl("https://github.com/TopherLee513/PSone-Memory-Manager/blob/master/README.md");
    	}
    	else
    		Statics.about = false;

		super.onPause();
	}
	

}
