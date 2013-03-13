package com.httpfiles;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class IdSender {

	final private String TAG = "SNnMB";
	private String fbName;
	private String twitterName;

	public void setId(String fbName, String twitterName){
		this.fbName=fbName;
		this.twitterName=twitterName;
	}

	public void sendIdToServer(){
		Thread th= new Thread(){
    		public void run(){
    			try{
    				HttpClient httpclient = new DefaultHttpClient();
    				String uri="http://studentweb.cs.bham.ac.uk/~axm514/getemailid.php?fb="+fbName+"&twitter="+twitterName;
    				Log.d(TAG, "Sending names to: "+uri);
    				HttpPost httppost = new   HttpPost(uri);  
    				HttpResponse response = httpclient.execute(httppost);
    				Log.d(TAG, "Success"+response.getParams());
    			} catch (MalformedURLException e) {
    				Log.e(TAG, e.toString());
    			} catch (IOException e) {
    				Log.e(TAG, e.toString());
    			}
    		}
    	};
    	th.start();		
	}
}