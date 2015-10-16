<<<<<<< HEAD:src/com/dlka/psonememorymanager/MCTabsWidget.java
package com.dlka.psonememorymanager;

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
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class MCTabsWidget extends TabActivity {
	private int numTabs;
	private ArrayList<TabHost.TabSpec> tablist = new ArrayList<TabHost.TabSpec>();
	TabHost tabHost;// = getTabHost();  // The activity TabHost
	Resources res;
	 String passedFN = new String();
	 Intent intent;
	 int totalTabs;
	 int count=0;
	 int bg=1;
	 
	 
//	  @Override
//	     public void onResume()
//	  {
//		  //getPrefs(); will FC onCreate because wallpaper isn't (why?) initialised, but if we write boolean wallpaper there it isn't global anymore
//		  boolean wallpaper=true; //set wallpaper and initialise it
//		  Map<String, ?> preferences;
//	    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//	    	preferences = prefs.getAll();
//	    	wallpaper = (Boolean) preferences.get("wallpaperPref");
//	    	
//		  Log.d("onResume", String.valueOf(wallpaper)); //debug print wallpaper
//		  if(wallpaper==false){
//			  //remove wallpaper
//			  //R.drawable.background=false;
//			  //android:background="@drawable/background" >
//			 //tabHost.setBackgroundResource(0);
//			// tabHost.getCurrentView().setBackgroundResource(0);
//			  tabHost.setBackgroundResource(0);
//		  }
//		  else{
//			  //display wallpaper
//			 //crash layout tabHost.getTabWidget().setBackgroundResource(R.drawable.background);
//			 tabHost.setBackgroundResource(R.drawable.background);
//			 //tabHost.getCurrentView().setBackgroundResource(R.drawable.background);
//		  }
//		  super.onResume();
//	  }
//	
	  @Override
	     public void onBackPressed()
	     {
	    	 
		 // this.getParent().onBackPressed();  
		 // this.finish(); 
		  Log.d("MCTabsWidget","inside onBackPressed()");
	    	
	    	if(count==0)
	    	{
	    		Toast.makeText(getApplicationContext(),("Press again to exit app"), Toast.LENGTH_SHORT).show();
	    		count++;
	    		return;
	    	}
	    	else if(count == 1)
	    	{
	    		//count=0;no need to reset, we do in onCreate, and super.onBackPressed destroys activity too
	    		super.onBackPressed();  // just in case	
	    	}
	    	else
	    	{ //if something wrong, better close app than make it unclosable
	    		 super.onBackPressed();  // just in case	
	    	}    
	     }
	  
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	TabHost.TabSpec tab = null;  // Resusable TabSpec for each tab
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    tabHost = getTabHost();
	    res = getResources(); // Resource object to get Drawables
	    	   
	    
	    intent = new Intent().setClass(this, MCViewActivity.class);
	    //Converted this to string(TL);
		intent.putExtra("com.dlka.psonememorymanager.FN", "No MC");
		intent.putExtra("tabid", 0);
		tab = tabHost.newTabSpec("card"+totalTabs).setIndicator("No MC",
	              res.getDrawable(R.drawable.memcard));
		tab.setContent(intent);
		tablist.add(tab);
		tabHost.addTab(tab);
		tabHost.setCurrentTab(0);
		intent = new Intent().setClass(this, MCViewActivity.class);
		intent.putExtra("com.dlka.psonememorymanager.FN", "No MC");
		intent.putExtra("tabid", 1);
		tab = tabHost.newTabSpec("card"+totalTabs).setIndicator("No MC",
	              res.getDrawable(R.drawable.memcard));
		//TL;
		tab.setContent(intent);
		tablist.add(tab);
		tabHost.addTab(tab);
		tabHost.setCurrentTab(1);
		tabHost.setCurrentTab(0);

		
		getPrefs();
		
	    totalTabs = 2;
	    numTabs = 2;	
	    count=0;
	}    
    
  
    
    public void getPrefs(){
    	Map<String, ?> preferences;
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	preferences = prefs.getAll();
    	Log.d("getPrefs","1");
    	Boolean backup = (Boolean) preferences.get("backupPref");
    	Log.d("getPrefs","2");
    	//wallpaper = (Boolean) preferences.get("wallpaperPref");
    	Log.d("getPrefs","3");
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
					passedFN = data.getStringExtra("com.dlka.psonememorymanager.FN");    			
					intent = new Intent().setClass(this, MCViewActivity.class);
					intent.putExtra("com.dlka.psonememorymanager.FN", passedFN);
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
	    		
	    		//String fn = data.getStringExtra("com.dlka.psonememorymanager.FN");
	    		intent = data;
	    		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
	    		final EditText input = new EditText(this);
	    		alert.setView(input);
	    		//Converted this to string(TL);
	    		alert.setTitle(getString (R.string.enter_filename));
	    		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog, int whichButton) {
	    				String value = input.getText().toString().trim();
	    				String fn = intent.getStringExtra("com.dlka.psonememorymanager.FN");
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
    	    						intent.putExtra("com.dlka.psonememorymanager.FN", passedFN);
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
        inflater.inflate(R.menu.main_activity_actions, menu);
        //inflater.inflate(R.menu.menu, menu);
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
    		//RelativeLayout rLayout = (RelativeLayout) vTabs.getChildAt(tabHost.getCurrentTab());
    		LinearLayout lLayout = (LinearLayout) vTabs.getChildAt(tabHost.getCurrentTab());
    		((TextView) lLayout.getChildAt(1)).setText(mcl.getDir());
    		Statics.cards[tabHost.getCurrentTab()] = mcl;
    		return true;	 
//TopherLee added; //moved by David-Lee Kulsch
        case R.id.exit:	
        	super.onBackPressed();
        	return true;//finish(); //still won't work, so removing temporary from preferencescreen
//(TL)	
        case R.id.prefs:
        	Intent i = new Intent().setClass(getApplicationContext(), Prefs.class);
        	startActivityForResult(i, 65532);
        	return true;
        case R.id.mail: //starting mail program and filling some defaults. English, no <string> at the moment.
        	StringBuffer buffer = new StringBuffer();
        	buffer.append("mailto:");
        	buffer.append("feedback-psonememorymanger@kulsch-it.de");
        	buffer.append("?subject=");
        	buffer.append("Feedback");
        	buffer.append("&body=");
        	buffer.append("Android Version:?\n Device-Model:?\nYour Message:");
        	String uriString = buffer.toString().replace(" ", "%20");
        	startActivity(Intent.createChooser(new Intent(Intent.ACTION_SENDTO, Uri.parse(uriString)), "Contact Developer"));
        	return true;
        case R.id.tooglebackground:
        	//if(bg==0){tabHost.setBackgroundResource(R.drawable.background);bg=1;}
        	// else { 
        	tabHost.setBackgroundResource(0);//bg=0;}
 			return true;
 			
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}


=======
package com.topherlee.psonememorymanager;

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
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import com.topherlee.psonememorymanager.R;

public class MCTabsWidget extends TabActivity {
	private int numTabs;
	private ArrayList<TabHost.TabSpec> tablist = new ArrayList<TabHost.TabSpec>();
	TabHost tabHost;// = getTabHost();  // The activity TabHost
	Resources res;
	 String passedFN = new String();
	 Intent intent;
	 int totalTabs;
	 int count = 0;
	 boolean bg = true;
	 
	  @Override
	     public void onBackPressed()
	     {
	    	 
		 // this.getParent().onBackPressed();  
		 // this.finish(); 
		  Log.d("MCTabsWidget","inside onBackPressed()");
	    	
	    	if(count==0)
	    	{
	    		Toast.makeText(getApplicationContext(),getString (R.string.back_to_exit), Toast.LENGTH_SHORT).show();
	    		count++;
	    		return;
	    	}
	    	else if(count == 1)
	    	{
	    		//count=0;no need to reset, we do in onCreate, and super.onBackPressed destroys activity too
	    		super.onBackPressed();  // just in case	
	    	}
	    	else
	    	{ //if something wrong, better close app than make it unclosable
	    		 super.onBackPressed();  // just in case	
	    	}    
	     }
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	TabHost.TabSpec tab = null;  // Resusable TabSpec for each tab
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    tabHost = getTabHost();
	    res = getResources(); // Resource object to get Drawables
	    	   
	    
	    intent = new Intent().setClass(this, MCViewActivity.class);
	    //Convert this to string(TL);
		intent.putExtra("com.topherlee.psonememorymanager.FN", "No MC");
		intent.putExtra("tabid", 0);
		tab = tabHost.newTabSpec("card"+totalTabs).setIndicator("No MC",
	              res.getDrawable(R.drawable.memcard));
		tab.setContent(intent);
		tablist.add(tab);
		tabHost.addTab(tab);
		tabHost.setCurrentTab(0);
		intent = new Intent().setClass(this, MCViewActivity.class);
		intent.putExtra("com.topherlee.psonememorymanager.FN", "No MC");
		intent.putExtra("tabid", 1);
		tab = tabHost.newTabSpec("card"+totalTabs).setIndicator("No MC",
	              res.getDrawable(R.drawable.memcard));
		//TL;
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
    	Log.d("getPrefs","1");
    	Boolean backup = (Boolean) preferences.get("backupPref");
    	Log.d("getPrefs","2");
     	//wallpaper = (Boolean) preferences.get("wallpaperPref");
       	Log.d("getPrefs","3");
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
					passedFN = data.getStringExtra("com.topherlee.psonememorymanager.FN");    			
					intent = new Intent().setClass(this, MCViewActivity.class);
					intent.putExtra("com.topherlee.psonememorymanager.FN", passedFN);
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
	    		
	    		//String fn = data.getStringExtra("com.topherlee.psonememorymanager.FN");
	    		intent = data;
	    		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
	    		final EditText input = new EditText(this);
	    		alert.setView(input);
	    		//Converted this to string(TL);
	    		alert.setTitle(getString (R.string.enter_filename));
	    		alert.setPositiveButton(getString (R.string.ok) , new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog, int whichButton) {
	    				String value = input.getText().toString().trim();
	    				String fn = intent.getStringExtra("com.topherlee.psonememorymanager.FN");
	    	    		if(fn!=null){
	    	    			try {
	    	    					File outFile = new File(fn+"/"+value+".mcr");
	    	    					
	    	    						outFile.createNewFile();
	    	    				    	    		    		    	    				
    	    						FileOutputStream out = new FileOutputStream(fn+"/"+value+".mcr");
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
    	    						passedFN = fn+"/"+value+".mcr";  			
    	    						intent = new Intent().setClass(getApplicationContext(), MCViewActivity.class);
    	    						intent.putExtra("com.topherlee.psonememorymanager.FN", passedFN);
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

	    		alert.setNegativeButton(getString (R.string.cancel),
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
        inflater.inflate(R.menu.main_activity_actions, menu);
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
    		in.putExtra("title", getString (R.string.long_press));
        	startActivityForResult(in, 65534);
        	return true;
        case R.id.save:
        	if(Statics.cards[tabHost.getCurrentTab()]==null){
        		Toast.makeText(getApplicationContext(), getString (R.string.nomc)+" "+(tabHost.getCurrentTab()+1)+".", Toast.LENGTH_SHORT).show();
        		return true;
        		
        	}

    		// add check for save backup flag
    		MemoryCard mcl = Statics.cards[tabHost.getCurrentTab()];
    		mcl.save();
    		Toast.makeText(getApplicationContext(), getString (R.string.saved_slot)+" "+(tabHost.getCurrentTab()+1)+".", Toast.LENGTH_SHORT).show();
    		TabWidget vTabs = getTabWidget();
    		//DLK
    		LinearLayout lLayout = (LinearLayout) vTabs.getChildAt(tabHost.getCurrentTab());
    		((TextView) lLayout.getChildAt(1)).setText(mcl.getDir());
    	    //
    		Statics.cards[tabHost.getCurrentTab()] = mcl;
    		return true;
//TopherLee added; //moved by David-Lee Kulsch
       case R.id.exit:	
       super.onBackPressed();
       return true;//finish();
//(TL)	
        case R.id.prefs:
        	Intent i = new Intent().setClass(getApplicationContext(), Prefs.class);
        	startActivityForResult(i, 65532);
        	return true;
        case R.id.feedback: //starting mail program and filling some defaults. English, no <string> at the moment.//(TL)Added a couple strings
        	StringBuffer buffer = new StringBuffer();
        	buffer.append("mailto:");
        	buffer.append(getString (R.string.email));
            buffer.append("?subject=");
           	buffer.append(getString (R.string.feedback));
          	buffer.append("&body=");
          	//buffer.append("Android Version:?\n Device-Model:?\nYour Message:");//(TL)
           	String uriString = buffer.toString().replace(" ", "%20");
        		 startActivity(Intent.createChooser(new Intent(Intent.ACTION_SENDTO, Uri.parse(uriString)), (getString (R.string.contact_developer))));
        		      return true;
         case R.id.togglebackground://(TL)changed to boolean
        		if(bg==false){tabHost.setBackgroundResource(R.drawable.background);bg=true;}
        		else { 
        		tabHost.setBackgroundResource(0);bg=false;}
        		return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}


>>>>>>> TopherLee513/master:src/com/topherlee/psonememorymanager/MCTabsWidget.java
