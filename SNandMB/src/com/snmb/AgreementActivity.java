package com.snmb;

import com.example.snandmb.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class AgreementActivity extends Activity {

    final private String TAG = "SNnMB";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
    }

    public void agreed(View v){
    	SharedPreferences sp= getSharedPreferences("snmbData", 0);
    	SharedPreferences.Editor ed =sp.edit();
    	ed.putBoolean("agree", true);
    	ed.commit();
    	Log.d(TAG, "Agreed");
		ActivityReturnCode rcode = new ActivityReturnCode();
    	setResult(rcode.AgreementActivity_Code);
    	finish();
    }

}
