package com.dlka.psonememorymanager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class EditHeader extends Activity {
    Spinner region;
    EditText prodID;
    EditText saveID;
    boolean bg = true;
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
        if (reg.contentEquals("BA")) {
            regsel = 0;
        } else if (reg.contentEquals("BI")) {
            regsel = 1;
        } else if (reg.contentEquals("BE")) {
            regsel = 2;
        }

        region.setSelection(regsel);
    }

    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
            case R.id.prefs:
                Intent i = new Intent().setClass(getApplicationContext(), Prefs.class);
                startActivityForResult(i, 65532);
                return true;
            case R.id.feedback: //starting mail program and filling some defaults. English, no <string> at the moment.//(TL)Added a couple strings
                StringBuffer buffer = new StringBuffer();
                buffer.append("mailto:");
                buffer.append(getString(R.string.email));
                buffer.append("?subject=");
                buffer.append(getString(R.string.feedback));
                buffer.append("&body=");
                //buffer.append("Android Version:?\n Device-Model:?\nYour Message:");//(TL)
                String uriString = buffer.toString().replace(" ", "%20");
                startActivity(Intent.createChooser(new Intent(Intent.ACTION_SENDTO, Uri.parse(uriString)), (getString(R.string.contact_developer))));
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public void editOK(View view) {
        intent = new Intent();
        intent.putExtra("saveid", saveID.getText().toString());
        intent.putExtra("prodid", prodID.getText().toString());
        intent.putExtra("region", region.getSelectedItemPosition());
        intent.putExtra("blocknum", blocknum);
        setResult(RESULT_OK, intent);
        finish();

    }
}
