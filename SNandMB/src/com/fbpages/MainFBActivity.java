package com.fbpages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.snandmb.R;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.android.Facebook.DialogListener;
import com.snmb.ActivityReturnCode;
import com.ubhave.sensormanager.data.SensorData;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainFBActivity extends Activity implements OnClickListener{

	Facebook fb;
	AsyncFacebookRunner asyncRunner;
	ImageView pic, button;
	TextView welcome;
	private SharedPreferences sp;
	Bitmap bmp;
	String post;
	String access_token; 
	long expires;
    final private String TAG = "SNnMB";
	final private Boolean lock = false;
	ActivityReturnCode rcode=new ActivityReturnCode();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_fb);	
		Log.e("ABC", "MainFBActivity Created"); 
		String appId= "518620884845095";    	
		fb = new Facebook(appId);
		asyncRunner = new AsyncFacebookRunner(fb);	    	
		button= (ImageView)findViewById(R.id.loginB);
		pic= (ImageView)findViewById(R.id.pic);
		welcome= (TextView) findViewById(R.id.welcome);
		sp=getSharedPreferences("snmbData",0);	         
		button.setOnClickListener(this);
		//updateButtonImage();
	}

	private void updateButtonImage() {
		if(fb.isSessionValid()){
			Toast.makeText(MainFBActivity.this, "Session Valid", Toast.LENGTH_SHORT).show();
			button.setImageResource(R.drawable.logout);
			button.setVisibility(Button.INVISIBLE);
			pic.setVisibility(ImageView.VISIBLE);

			AsyncTask.execute(new Runnable(){
				public void run(){
					try {
						JSONObject obj =null;
						URL img_url = null;
						String jsonUser =fb.request("me");							
						obj=Util.parseJson(jsonUser);

						String id = obj.optString("id");
						String name =obj.optString("name");
						String username =obj.optString("username");	
						Editor editor =sp.edit();
						editor.putString("id", id);
						editor.putString("name", name);
						editor.putString("fbusername", username);
						//editor.putString("stream", (String) obj.opt("feed"));
						editor.commit();

						File file = new File(Environment.getExternalStorageDirectory(), "emoext.txt");
						try {

							FileOutputStream os = new FileOutputStream(file); 
							OutputStreamWriter out = new OutputStreamWriter(os);
							out.write(sp.getString("access_token", null));
							out.close();
						} catch (IOException e) {
							Log.e(getCallingPackage(), "Error logging the file");
						}

						img_url =new URL("http://graph.facebook.com/"+id+"/picture?type=large");
						bmp =BitmapFactory.decodeStream(img_url.openConnection().getInputStream());

					} catch (NullPointerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch (FacebookError e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
			});

		}
		else{
			Toast.makeText(MainFBActivity.this, "InValid Session", Toast.LENGTH_SHORT).show();
			button.setImageResource(R.drawable.login);
			pic.setVisibility(ImageView.VISIBLE);
		}
	}

	public void launchB(View v) throws MalformedURLException, IOException{
		//Toast.makeText(MainActivity.this, "launchB", Toast.LENGTH_SHORT).show();
		Toast.makeText(MainFBActivity.this, sp.getString("from", null), Toast.LENGTH_SHORT).show();
		welcome.setText("Welcome "+sp.getString("name", null)+", your access_token is: "+ sp.getString("access_token", null));
		pic.setImageBitmap(bmp);
		updateButtonImage();			
	}


	public void onClick(View v){
		Log.e("ABC", "MAinFBActivity: opening fblogin");
		Intent i=new Intent(MainFBActivity.this,FBLogin.class);
		startActivity(i);
		SharedPreferences sp= getSharedPreferences("snmbData", 0);
    	SharedPreferences.Editor ed =sp.edit();
    	ed.putBoolean("backKey", false);
    	ed.commit();
		setResult(rcode.FBActivity_Code);
    	finish();
//		Log.d(Tag, "Trying to login");		
//		tryLogin();
//		Log.d(Tag, sp.getString("access_token", "not found"));
//		insertFile(sp.getString("access_token", "not found"));
//		new abcd().execute();
	}
	
	public class abcd extends AsyncTask<Void, Void, String>{

		protected String doInBackground(Void... params) {
			tryLogin();
			return sp.getString("access_token", "not found");
		}
		
		protected void onPostExecute(String result) {
			Log.d(TAG, "Writing file");
			insertFile(result);
			super.onPostExecute(result);
		}
		
	}

	@SuppressWarnings("deprecation")
	public void tryLogin(){
		//updateButtonImage();
		//		if(fb.isSessionValid()){
		//			try {
		//				Toast.makeText(MainFBActivity.this, "tryLogin if", Toast.LENGTH_SHORT).show();
		//				fb.logout(MainFBActivity.this);			
		//				updateButtonImage();
		//			}catch (MalformedURLException e) {
		//				e.printStackTrace();
		//			} catch (IOException e) {
		//				e.printStackTrace();
		//			}	                                    
		//		}
		//		else{
		Log.d(TAG, "Trying to login else part");
		fb.authorize(MainFBActivity.this, new DialogListener() {					
			public void onFacebookError(FacebookError e) {
				Log.d(TAG, "FB Error");
			}					
			public void onError(DialogError e) {
				Log.d(TAG, "D Error");
			}					
			public void onComplete(Bundle values) {
				Log.d(TAG, "on Complete");
				Editor editor =sp.edit();
				editor.putString("access_token", fb.getAccessToken());
				editor.putLong("access_expires", fb.getAccessExpires());
				editor.commit();
				insertFile(sp.getString("access_token", "not found"));
			}					
			public void onCancel() {
				Log.d(TAG, "on Cancel");
			}
		});
		//}
		if(sp.getString("access_token", "0").equals("not found") || sp.getString("access_token", "0").equals("0")){
			Log.d(TAG, "Session Not Valid, trying again");
			new abcd().execute();
		}else{
			Log.d(TAG, "Session Valid");
		}
	}

	public void insertFile(String string){

		File file = new File(Environment.getExternalStorageDirectory(), "emoext.txt");
		try {

			FileOutputStream os = new FileOutputStream(file); 
			OutputStreamWriter out = new OutputStreamWriter(os);
			out.write(string);
			out.close();
		} catch (IOException e) {
			Log.e(getCallingPackage(), "Error logging the file");
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		//fb.authorizeCallback(requestCode, resultCode, data);
		SharedPreferences sp= getSharedPreferences("snmbData", 0);
    	SharedPreferences.Editor ed =sp.edit();
    	ed.putBoolean("backKey", false);
    	ed.commit();
    	Log.d("ABC", "back to MAinFBActivity onStart"+ sp.getBoolean("email", false));
		setResult(rcode.FBActivity_Code);
    	finish();
	}


}
