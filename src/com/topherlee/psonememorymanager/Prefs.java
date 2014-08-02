package com.topherlee.psonememorymanager;

import java.util.Map;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
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
		super.onPause();
	}
}
