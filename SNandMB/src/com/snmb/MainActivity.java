package com.snmb;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.snandmb.R;
import com.example.snandmb.R.layout;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.override;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.fbpages.FBLogin;
import com.fbpages.MainFBActivity;
import com.httpfiles.IdSender;
import com.twitterpages.TwitterActivity;

public class MainActivity extends Activity {

	Facebook fb;
	SharedPreferences sp;
	ActivityReturnCode rcode;
	final private String TAG = "SNnMB";
	private UiLifecycleHelper uiHelper;
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(TAG, "MAinActivity onCreate");
		sp=getSharedPreferences("snmbData",0);
		rcode=new ActivityReturnCode();
		Toast.makeText(this, "create", Toast.LENGTH_SHORT).show();
		if(sp.getBoolean("agree", false)==false){
			Log.d(TAG, "mainactivity: consent false");
			Intent i =new Intent(this, AgreementActivity.class);
			startActivityForResult(i, rcode.MainActivity_Code);
		}
		else if(sp.getBoolean("fblogin", false)==false){ 
			Log.d(TAG, "mainactivity: fblogin false");
			Intent i =new Intent(this, FBLogin.class);
			startActivityForResult(i, rcode.MainActivity_Code);
		}
		else if(sp.getBoolean("twitterlogin", false)==false){ 
			Log.d(TAG, "mainactivity: twitterlogin false");
			Intent i =new Intent(this, TwitterActivity.class);
			startActivityForResult(i, rcode.MainActivity_Code);
		}
		else{
			Log.d(TAG, "mainactivity: linking to StartService Activity");
			Intent i =new Intent(this, ServiceStartActivity.class);
			startActivityForResult(i, rcode.MainActivity_Code);
		}

	}

	@override
	public void onStart(){
		super.onStart();
		Log.d(TAG, "MAinActivity onStart");
		//Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();	
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "MAinActivity onActivityResult"+resultCode);
		if(resultCode==rcode.AgreementActivity_Code){
			Intent i =new Intent(this, FBLogin.class);
			startActivityForResult(i, rcode.MainActivity_Code);
		}
		else if(resultCode==rcode.FBActivity_Code){
			Intent i =new Intent(this, TwitterActivity.class);
			startActivityForResult(i, rcode.MainActivity_Code);
		}
		else if(resultCode==rcode.TwitterActivity_Code){
			sp=getSharedPreferences("snmbData",0);
			if(sp.getBoolean("twitterlogin", false)&&sp.getBoolean("fblogin", false)&& !sp.getBoolean("nameSent", false)){
				Editor ed=sp.edit();
				ed.putBoolean("nameSent", true);
				ed.commit();
				IdSender ids=new IdSender();
				ids.setId(sp.getString("fbusername", "NA"), sp.getString("twitterusername", "NA"));
				ids.sendIdToServer();
				Log.d(TAG, "Inside if");
			}
			Log.d(TAG, "Values:"+sp.getBoolean("fblogin", false)+sp.getBoolean("twitterlogin", false)
					+sp.getBoolean("nameSent", false));
			Intent i =new Intent(this, ServiceStartActivity.class);
			startActivityForResult(i, rcode.MainActivity_Code);
		}
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}