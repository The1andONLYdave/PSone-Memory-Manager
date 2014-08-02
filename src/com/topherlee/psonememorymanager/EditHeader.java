package com.topherlee.psonememorymanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import com.topherlee.psonememorymanager.R;

public class EditHeader extends Activity{
	Spinner region;
	EditText prodID;
	EditText saveID;
	
	int blocknum;
	Intent intent;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editheader);
		
		intent = getIntent();
		this.setTitle(intent.getStringExtra("title"));
		region = (Spinner) findViewById(R.id.spinner);
		prodID = (EditText) findViewById(R.id.prodEntry);
		saveID = (EditText) findViewById(R.id.idEntry);
		saveID.setText(intent.getStringExtra("saveid"));
		prodID.setText(intent.getStringExtra("prodid"));
		blocknum = intent.getIntExtra("blocknum", 0);
		String reg = intent.getStringExtra("region");
		int regsel = 0;
		if(reg.contentEquals("BA")){
			regsel = 0;
		}
		else if(reg.contentEquals("BI")){
			regsel = 1;
		}
		else if(reg.contentEquals("BE")){
			regsel = 2;
		}
		
		region.setSelection(regsel);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		              	
    			
	}
		
	public void editOK(View view){
		intent = new Intent();		
		intent.putExtra("saveid", saveID.getText().toString());
		intent.putExtra("prodid", prodID.getText().toString());
		intent.putExtra("region", region.getSelectedItemPosition());
		intent.putExtra("blocknum", blocknum);
		setResult(RESULT_OK, intent);  
		finish();
		
	}
}
