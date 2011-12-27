package com.tutorials.hellotabwidget;

import java.io.File;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.neko68k.psxmc.R;

public class MCViewActivityTab2 extends ListActivity{
	
	
	public void onCreate(Bundle savedInstanceState) {
		String fn = new String();
		Intent intent = getIntent();
		fn = intent.getStringExtra("com.tutorials.hellotabwidget.FN");		
        super.onCreate(savedInstanceState);        
        final ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                onListItemClick(lv, v,pos,id);
            }
        });        
        MemoryCard MC = new MemoryCard(new File(fn), getApplicationContext());
        DescIconListAdapter dila = new DescIconListAdapter(this);
        for(int i=0;i<15;i++){
        	DescIconList dil = MC.genList(i);
        	dila.addItem(dil);
        }
        this.setListAdapter(dila);
        
}
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
            int selectionRowID = (int)position;
            
    }
}
