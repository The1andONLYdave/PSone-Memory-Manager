package com.topherlee.psonememorymanager;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MCViewActivity extends ListActivity{ //extends ActionBarActivity{;
	private static MemoryCard MC;
	private DescIconListAdapter dila;
	private int isRunning = 0;
	private BroadcastReceiver receiver;
	//private int tabID;
	private int testid;
	private boolean changed = false;
	public void onCreate(Bundle savedInstanceState) {
		//MC=null;
		//dila=null;
		Random rand = new Random(System.currentTimeMillis());
		//tabID = rand.nextInt();
		String fn = new String();
		Intent intent = getIntent();
		fn = intent.getStringExtra("com.topherlee.psonememorymanager.FN");		
        super.onCreate(savedInstanceState);        
        final ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                onListItemClick(lv, v,pos,id);
            }
        });
        
        
        
        dila = new DescIconListAdapter(this);
        //testid = -1;
        testid = intent.getIntExtra("tabid", 0);
        if(fn!="No MC"){
        	MC = new MemoryCard(new File(fn), getApplicationContext());
        	//Statics.cards.put(tabID, MC);
        	
        	Statics.cards[testid]=MC;
	        for(int i=0;i<15;i++){
	        	DescIconList dil = MC.genList(i);
	        	dila.addItem(dil);
	        }
        } 
        this.setListAdapter(dila);
        
        //super.onResume();
        
        receiver = new BroadcastReceiver(){
        	@Override
        	public void onReceive(Context context, Intent intent){
        		int id = intent.getIntExtra("id", 0);
        		if(id!=testid){
        			
        			ArrayList<dirFrame> frames = intent.getParcelableArrayListExtra("frames");
        			//MC = Statics.cards.get(tabID);
        			MemoryCard mcl = Statics.cards[testid];
        			
        			if(mcl.getNumEmpties()<frames.size()){     
        				//Converted this to string(TL);
        				CharSequence text = getString(R.string.memory_low);
        				//;
        				int duration = Toast.LENGTH_SHORT;

        				Toast toast = Toast.makeText(context, text, duration);
        				toast.show();
        				//setResultCode(RESULT_CANCELED);
        				abortBroadcast();
        				return;
        			}
        			ArrayList<saveBlock> blocks = intent.getParcelableArrayListExtra("blocks");
        			
        			
        			// check for duplicate save id's
        			for(int i = 0; i<15; i++){
        				dirFrame tempframe = mcl.getFrame(i);
        				if(tempframe.getID().contentEquals(frames.get(0).getID())&&tempframe.getProd().contentEquals(frames.get(0).getProd())){
            				//Converted this to string(TL);
        					Toast.makeText(context, getString(R.string.saveid_lic), Toast.LENGTH_LONG).show();
        					/*intent = new Intent();
        					intent.setClass(getApplicationContext(), EditHeader.class);
        		    		intent.putExtra("prodid", new String(mcl.getFrame(i).getProd()));
        		    		intent.putExtra("saveid", new String(mcl.getFrame(i).getID()));
        		    		intent.putExtra("region", new String(mcl.getFrame(i).getRegion()));
        		    		intent.putExtra("title", new String(mcl.getBlock(i).getTitle()));
        		    		intent.putExtra("blocknum", i);
        		    		startActivityForResult(intent, 65531);
        					i=15;*/
        		    	}
        				
        		    		    
        			}
        			        			
        			for(int i = 0;i<frames.size();i++){        				
        				dirFrame frame = frames.get(i);
        				if(frames.size()>i+1){
        					frame.setNext((short)(mcl.getEmpty(1)));
        				}
        				mcl.setFrame(mcl.getEmpty(0), frame);
        				mcl.setBlock(mcl.getEmpty(0), blocks.get(i));
        				mcl.remEmpty(0);
        				
        			}
        			Statics.cards[testid]=mcl;
        			
        			saveBlock block = blocks.get(0);
        			
        			
        			
        			update();
    				Statics.changed = true;
    				//Converted this to string(TL);
    				Toast.makeText(context, getString(R.string.copied) +blocks.get(0).getTitle(), Toast.LENGTH_SHORT).show();
    				//;
        			abortBroadcast();
        			return;
        		}        		
        	}
        };
        

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.topherlee.psonememorymanager.COPYMC");
        registerReceiver(receiver, filter);

	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState){
		super.onSaveInstanceState(savedInstanceState);
		//savedInstanceState.
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		//savedInstanceState.
	}
	
	@Override 
	protected void onResume(){
		/*if(Statics.changed){
			MemoryCard mcl = Statics.cards[testid];
			dila.clear();
			for(int i=0;i<15;i++){
				
	        	DescIconList dil = mcl.genList(i);
	        	dila.addItem(dil);
	        }
	    	
			this.setListAdapter(dila);
			changed = false;
		}*/
		if(Statics.changed){
			update();
			Statics.changed = false;
		}
        super.onResume();

	}
		
	@Override
	public void onPause(){
		super.onPause();
	}
	
	@Override
	public void onStop(){
		//Statics.cards.remove(tabID);
		super.onStop();
	}
	
	private void update(){
		//if(Statics.changed){
			MemoryCard mcl = Statics.cards[testid];
			//dila = new DescIconListAdapter(this);
			dila.clear();
			for(int i=0;i<15;i++){
				
	        	DescIconList dil = mcl.genList(i);
	        	dila.addItem(dil);
	        }
			//dila.notifyDataSetInvalidated();
			//dila.notifyDataSetChanged();
			dila.notifyDataSetChanged();
			//this.setListAdapter(dila);
			
			//changed = false;
		//}
	}
	
	public void sendBroadcast(int num){
		dirFrame frame;
		dirFrame frames[];
		saveBlock blocks[];
		Intent broadcast = new Intent();
		broadcast.setAction("com.topherlee.psonememorymanager.COPYMC");
		MemoryCard mcl = Statics.cards[testid];
		frame = mcl.getFrame(num);
		if(frame.getSize()>8192){
			int numBlocks = frame.getSize()/8192;
			blocks = new saveBlock[numBlocks];
			frames = new dirFrame[numBlocks];
			
			for(int i = 0; i<numBlocks;i++){
				frames[i] = mcl.getFrame(num);
				blocks[i] = mcl.getBlock(num);
				num = frames[i].getNext();
			}
		} else {
			blocks = new saveBlock[1];
			frames = new dirFrame[1];
			frames[0] = frame;
			blocks[0] = mcl.getBlock(num);
		}
		//block = MC.getBlock(i);
		//broadcast.putExtra("frame", frame);
		//broadcast.putExtra("block", block);
		
		broadcast.putParcelableArrayListExtra("frames", new ArrayList<dirFrame>(Arrays.asList(frames)));
		broadcast.putParcelableArrayListExtra("blocks", new ArrayList<saveBlock>(Arrays.asList(blocks)));
		broadcast.putExtra("id", testid);
		//Statics.cards.put(tabID, MC);
		sendOrderedBroadcast(broadcast, null);
		
	}
	
	@Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
	    

    	
    	
    	if(resultCode == RESULT_OK){
    		// edit details
    		if(requestCode == 65531){    	    			
    			MemoryCard mcl = Statics.cards[testid];    			
    			int blocknum = data.getIntExtra("blocknum", 0);
    			dirFrame frame = mcl.getFrame(blocknum);
    			String temp = data.getStringExtra("saveid");
    			frame.setID(temp);
    			frame.setProd(data.getStringExtra("prodid"));
    			frame.setRegion(data.getIntExtra("region", 0));
    			frame.updateXor();
    			mcl.setFrame(blocknum, frame);
    			Statics.cards[testid] = mcl;
    			Statics.changed = true;
    			update();
    		}
    		// open mc
	    	if(requestCode == 65533){  
	    		
	    		//dila = new DescIconListAdapter(this);
	            //testid = -1;
	            String fn = data.getStringExtra("com.topherlee.psonememorymanager.FN");
	            int savenum = data.getIntExtra("savenum", 0);
	            
	            MemoryCard mcl = Statics.cards[testid];
	    		dirFrame frame = mcl.getFrame(savenum);
	    		int numBlocks = frame.getSize()/8192;
	    		String outfn = new String(fn+"/"+frame.getProd()+"_"+frame.getID()+".mcs");
	    		File outFile = new File(outfn);
				
				try {
					outFile.createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		String savename = new String(outfn);
	    		FileOutputStream out = null;
	    		DataOutputStream outStream = null;
				try {
					out = new FileOutputStream(savename);
					outStream = new DataOutputStream(out);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
				frame.write(outStream);
				
	    		if(numBlocks>1){

	    			/*for(int i = 0; i<numBlocks;i++){
	    				frame = mcl.getFrame(savenum+i);	
	    				frame.write(outStream);
	    			}*/
	    			boolean header = true;
	    			for(int i = 0; i<numBlocks;i++){
	    				saveBlock block = mcl.getBlock(savenum+i);
	    				block.save(outStream, header);
	    				if(header == true)
	    					header = false;
	    			}
	    		} else {
	    			
	    			mcl.getBlock(savenum).save(outStream, false);	    			
	    		}
	    		try {
					outStream.flush();
					outStream.close();
		    		out.flush();
		    		out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	            
	    	}
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }

	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
            final int selectionRowID = (int)position;
            //TODO: Convert this to string(TL);
            final CharSequence[] items = {"Edit Details", "Copy", "Delete", "Restore"};//, 
    				//"Import", "Export"};

    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		DescIconList dil = (DescIconList) dila.getItem(selectionRowID);
    		
    		builder.setTitle(dil.getText());
    		builder.setItems(items, new DialogInterface.OnClickListener() {
    		    public void onClick(DialogInterface dialog, int item) {
    		    	if(items[item]=="Copy"){
    		    		MemoryCard mcl = Statics.cards[testid];
    		    		if(mcl.getFrame(selectionRowID).getType()>81)
    		    			//TODO: Convert this to string(TL);
    		    			Toast.makeText(getApplicationContext(), "Cannot directly copy linked blocks.", Toast.LENGTH_SHORT).show();
    		    		else
    		    			sendBroadcast(selectionRowID);
    		    	}
    		    	if(items[item]=="Delete"){
    		    		MemoryCard mcl = Statics.cards[testid];
    		    		if(mcl.getFrame(selectionRowID).getType()>81)
    		    			//TODO: Convert this to string(TL);
    		    			Toast.makeText(getApplicationContext(), "Cannot directly delete linked blocks.", Toast.LENGTH_SHORT).show();
    		    		else{
    		    		mcl.delete(selectionRowID);
    		    		Statics.cards[testid] = mcl;    		    		
    		    		update();    		    		
    		    		Statics.changed = true;
    		    		
    		    		Toast.makeText(getApplicationContext(), mcl.getTitle(selectionRowID), Toast.LENGTH_SHORT).show();
    		    		}
    		    	}
    		    	if(items[item]=="Restore"){
    		    		MemoryCard mcl = Statics.cards[testid];
    		    		if(mcl.getFrame(selectionRowID).getType()>161)
    		    			//TODO: Convert this to string(TL);
    		    			Toast.makeText(getApplicationContext(), "Cannot directly restore linked blocks.", Toast.LENGTH_SHORT).show();
    		    		else if(mcl.getFrame(selectionRowID).getType()<161)
    		    			return;
    		    		else{
    		    			boolean result = mcl.unDelete(selectionRowID);
    		    			if(result){
    		    				//TODO: Convert this to string(TL);
    		    				Toast.makeText(getApplicationContext(), "Successfully restored ."+mcl.getTitle(selectionRowID), Toast.LENGTH_SHORT).show();
    		    				update();
    		    				Statics.changed = true;
    		    			} else {
    		    				//TODO: Convert this to string(TL);
    		    				Toast.makeText(getApplicationContext(), "Could not restore ."+mcl.getTitle(selectionRowID), Toast.LENGTH_SHORT).show();
    		    			}
    		    		}
    		    	}
    		    	if(items[item]=="Export"){
    		    		Intent intent = new Intent();
    		    		intent.setClass(getApplicationContext(), FileBrowser.class);//  .setClass(this, FileBrowser.class);
    		    		intent.putExtra("dirpick", true);
    		    		//TODO: Convert this to string(TL);
    		    		intent.putExtra("title", "Long Press To Select Folder");
    		    		intent.putExtra("savenum", selectionRowID);
    		        	startActivityForResult(intent, 65533);
    		    	}
    		    	if(items[item]=="Edit Details"){
    		    		MemoryCard mcl = Statics.cards[testid];
    		    		if(mcl.getFrame(selectionRowID).getType()==82||mcl.getFrame(selectionRowID).getType()==83){
    		    			Toast.makeText(getApplicationContext(), "Cannot edit linked blocks.", Toast.LENGTH_SHORT).show();
    		    			return;
    		    		}
    		    		if(mcl.getFrame(selectionRowID).getType()==160){
    		    			Toast.makeText(getApplicationContext(), "This block is empty.", Toast.LENGTH_SHORT).show();
    		    			return;
    		    		}
    		    		
    		    		Intent intent = new Intent();
    		    		intent.setClass(getApplicationContext(), EditHeader.class);
    		    		intent.putExtra("prodid", new String(mcl.getFrame(selectionRowID).getProd()));
    		    		intent.putExtra("saveid", new String(mcl.getFrame(selectionRowID).getID()));
    		    		intent.putExtra("region", new String(mcl.getFrame(selectionRowID).getRegion()));
    		    		intent.putExtra("title", new String(mcl.getBlock(selectionRowID).getTitle()));
    		    		intent.putExtra("blocknum", selectionRowID);
    		    		startActivityForResult(intent, 65531);
    		    	}
    		        
    		    }
    		});
    		AlertDialog alert = builder.create();
    		alert.show();
    }
	
	
}
