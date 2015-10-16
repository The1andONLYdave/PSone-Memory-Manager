package com.dlka.psonememorymanager;

import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
<<<<<<< HEAD:src/com/dlka/psonememorymanager/Prefs.java
=======
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

import com.topherlee.psonememorymanager.R;
>>>>>>> TopherLee513/master:src/com/topherlee/psonememorymanager/Prefs.java

public class Prefs extends PreferenceActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);            
            getActionBar().setDisplayHomeAsUpEnabled(true);//Up Navigation           
            setResult(RESULT_OK);
            
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	    }
	    return super.onOptionsItemSelected(item);
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
