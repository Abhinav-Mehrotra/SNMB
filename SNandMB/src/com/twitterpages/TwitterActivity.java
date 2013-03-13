package com.twitterpages;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.snandmb.R;
import com.snmb.ActivityReturnCode;

public class TwitterActivity extends Activity {

	SharedPreferences sp;
	final private String TAG = "SNnMB";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
        ImageView button=(ImageView)findViewById(R.id.loginTwitter);
        button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				tryTwitterLogin();
			}
		});
    }
    
    public void tryTwitterLogin(){
    	Intent i=new Intent(this, TwitterLoginActivity.class);
    	startActivityForResult(i,0);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		sp=getSharedPreferences("snmbData",0);
		ActivityReturnCode arc=new ActivityReturnCode();
		if(sp.getBoolean("twitterlogin", false)){
			setResult(arc.TwitterActivity_Code);
		}else{
			setResult(arc.FBActivity_Code);
		}
		finish();
	}
}
