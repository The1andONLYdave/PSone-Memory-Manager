package com.tutorials.hellotabwidget;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import com.neko68k.psxmc.R;

public class MCTabsWidget extends TabActivity {
	private int numTabs;
	private ArrayList<TabHost.TabSpec> tablist = new ArrayList<TabHost.TabSpec>();
	TabHost tabHost;// = getTabHost();  // The activity TabHost
	Resources res;
	 String passedFN = new String();
	 Intent intent;
	 int totalTabs;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	TabHost.TabSpec tab = null;  // Resusable TabSpec for each tab
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    tabHost = getTabHost();
	    res = getResources(); // Resource object to get Drawables
	    	   
	    
	    intent = new Intent().setClass(this, MCViewActivity.class);
		intent.putExtra("com.tutorials.hellotabwidget.FN", "No MC");
		intent.putExtra("tabid", 0);
		tab = tabHost.newTabSpec("card"+totalTabs).setIndicator("No MC",
	              res.getDrawable(R.drawable.memcard));
		tab.setContent(intent);
		tablist.add(tab);
		tabHost.addTab(tab);
		tabHost.setCurrentTab(0);
		intent = new Intent().setClass(this, MCViewActivity.class);
		intent.putExtra("com.tutorials.hellotabwidget.FN", "No MC");
		intent.putExtra("tabid", 1);
		tab = tabHost.newTabSpec("card"+totalTabs).setIndicator("No MC",
	              res.getDrawable(R.drawable.memcard));
		tab.setContent(intent);
		tablist.add(tab);
		tabHost.addTab(tab);
		tabHost.setCurrentTab(1);
		tabHost.setCurrentTab(0);

		
		getPrefs();
		
	    totalTabs = 2;
	    numTabs = 2;	    
	}    
    
    public void getPrefs(){
    	Map<String, ?> preferences;
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	preferences = prefs.getAll();
    	Boolean backup = (Boolean) preferences.get("backupPref");
    	if(backup==null||backup==true){
    		Statics.backup = true;
    	}
    	else
    		Statics.backup = false;
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
	    super.onActivityResult(requestCode, resultCode, data);
    	
    	if(resultCode == RESULT_OK){
    		// open mc
	    	if(requestCode == 65535){    		    		
				 if(numTabs == 2){
					TabHost.TabSpec tab1 = null;
					int curTab = tabHost.getCurrentTab();
					tablist.remove(curTab);
					tabHost.setCurrentTab(0);
					tabHost.clearAllTabs();
					passedFN = data.getStringExtra("com.tutorials.hellotabwidget.FN");    			
					intent = new Intent().setClass(this, MCViewActivity.class);
					intent.putExtra("com.tutorials.hellotabwidget.FN", passedFN);
					intent.putExtra("tabid", curTab);
					totalTabs++;
					tab1 = tabHost.newTabSpec("card"+(curTab)+totalTabs).setIndicator(passedFN,
				              res.getDrawable(R.drawable.memcard));
					tab1.setContent(intent);    				
					tablist.add(curTab, tab1);    				
					
					tabHost.addTab(tablist.get(0));
					tabHost.addTab(tablist.get(1));
					tabHost.setCurrentTab(curTab);
				}
	    	}
	    	
	    	// preferences return
	    	if(requestCode == 65532){
	    		getPrefs();
	    	}
	    	// new card
	    	if(requestCode == 65534){
	    		
	    		//String fn = data.getStringExtra("com.tutorials.hellotabwidget.FN");
	    		intent = data;
	    		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
	    		final EditText input = new EditText(this);
	    		alert.setView(input);
	    		alert.setTitle("Enter new filename.");
	    		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog, int whichButton) {
	    				String value = input.getText().toString().trim();
	    				String fn = intent.getStringExtra("com.tutorials.hellotabwidget.FN");
	    	    		if(fn!=null){
	    	    			try {
	    	    					File outFile = new File(fn+"/"+value+".mcd");
	    	    					
	    	    						outFile.createNewFile();
	    	    				    	    		    		    	    				
    	    						FileOutputStream out = new FileOutputStream(fn+"/"+value+".mcd");
    	    						InputStream in = getApplicationContext().getResources().openRawResource(R.raw.blank_card);
    	    						BufferedOutputStream dest = new BufferedOutputStream(out, 0x1FFFE);
    	    						byte[] buffer = new byte[0x1FFFE];
    	    						in.read(buffer);
    	    						dest.write(buffer);
    	    						in.close();
    	    						dest.flush();
    	    						dest.close();
    	    						
    	    						TabHost.TabSpec tab1 = null;
    	    						int curTab = tabHost.getCurrentTab();
    	    						tablist.remove(curTab);
    	    						tabHost.setCurrentTab(0);
    	    						tabHost.clearAllTabs();
    	    						passedFN = fn+"/"+value+".mcd";  			
    	    						intent = new Intent().setClass(getApplicationContext(), MCViewActivity.class);
    	    						intent.putExtra("com.tutorials.hellotabwidget.FN", passedFN);
    	    						intent.putExtra("tabid", curTab);
    	    						totalTabs++;
    	    						tab1 = tabHost.newTabSpec("card"+(curTab)+totalTabs).setIndicator(passedFN,
    	    					              res.getDrawable(R.drawable.memcard));
    	    						tab1.setContent(intent);    				
    	    						tablist.add(curTab, tab1);    				
    	    						
    	    						tabHost.addTab(tablist.get(0));
    	    						tabHost.addTab(tablist.get(1));
    	    						tabHost.setCurrentTab(curTab);
    	    						

	    	    		    	
	    	    			} catch (Exception e) {
	    	    				// TODO Auto-generated catch block
	    	    				e.printStackTrace();
	    	    			}
	    	    		}
	    			}
	    		});

	    		alert.setNegativeButton("Cancel",
	    				new DialogInterface.OnClickListener() {
	    					public void onClick(DialogInterface dialog, int whichButton) {
	    						dialog.cancel();
	    					}
	    				});
	    		alert.show();
	    		
	    	}
    	}
    }
    

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.open:
        	Intent intent = new Intent().setClass(this, FileBrowser.class);
        	startActivityForResult(intent, 65535);
            return true;        
        case R.id.create:
        	Intent in= new Intent();
    		in.setClass(getApplicationContext(), FileBrowser.class);//  .setClass(this, FileBrowser.class);
    		in.putExtra("dirpick", true);
    		in.putExtra("title", "Long Press To Select Folder");
        	startActivityForResult(in, 65534);
        	return true;
        case R.id.save:
        	if(Statics.cards[tabHost.getCurrentTab()]==null){
        		Toast.makeText(getApplicationContext(), "No MC loaded in slot "+(tabHost.getCurrentTab()+1)+".", Toast.LENGTH_SHORT).show();
        		return true;
        	}
        		
    		
    		// add check for save backup flag
    		MemoryCard mcl = Statics.cards[tabHost.getCurrentTab()];
    		mcl.save();
    		Toast.makeText(getApplicationContext(), "Saved slot "+(tabHost.getCurrentTab()+1)+".", Toast.LENGTH_SHORT).show();
    		TabWidget vTabs = getTabWidget();
    		RelativeLayout rLayout = (RelativeLayout) vTabs.getChildAt(tabHost.getCurrentTab());
    		((TextView) rLayout.getChildAt(1)).setText(mcl.getDir());
    		Statics.cards[tabHost.getCurrentTab()] = mcl;
    		return true;	 
        case R.id.prefs:
        	Intent i = new Intent().setClass(getApplicationContext(), Prefs.class);
        	startActivityForResult(i, 65532);
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}


