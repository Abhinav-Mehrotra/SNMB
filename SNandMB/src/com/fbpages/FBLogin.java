package com.fbpages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.snandmb.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.model.GraphUser;
import com.snmb.ActivityReturnCode;
import com.snmb.MainActivity;
import com.snmb.ServiceStartActivity;

public class FBLogin extends Activity {
	Facebook fb;
	SharedPreferences sp;
	final private String TAG = "SNnMB";
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fblogin);
		Log.d("ABC", "onStart FBLogin Activity");
		ImageView button= (ImageView)findViewById(R.id.loginB);
		button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					tryFbLogin();
				}
			});		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try
		{
			Log.e(TAG, "OnActivityResult FBLogin Activity");
			super.onActivityResult(requestCode, resultCode, data);
			//fb.authorizeCallback(requestCode, resultCode, data);
			Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		}
		catch(Exception e)
		{
			Log.e(TAG, "Catch FBLogin Activity");
			Toast.makeText(getApplicationContext(), "Something went wrong Try again Later!!", Toast.LENGTH_LONG).show();
		}
	}
	
	public void tryFbLogin(){
		Session.openActiveSession(FBLogin.this, true, new Session.StatusCallback() {
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {
					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
						public void onCompleted(GraphUser user, Response response) {
							if (user != null) {
								sp=getApplicationContext().getSharedPreferences("snmbData",0);
								SharedPreferences.Editor editor= sp.edit();
								editor.putString("fbusername", user.getUsername());
								editor.putBoolean("fblogin", true);								
								editor.commit();
								Log.d(TAG, sp.getString("fbusername", "not found"));
								if(!sp.getString("user_name", "not found").equals("not found")){
									ActivityReturnCode rcode=new ActivityReturnCode();
									setResult(rcode.AgreementActivity_Code);
									finish();
								}
								else{
									ActivityReturnCode rcode=new ActivityReturnCode();
									setResult(rcode.FBActivity_Code);
									finish();
								}
							}
						}
					});
				}
			}
		});
	}

}
