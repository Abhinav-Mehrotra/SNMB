package com.twitterpages;

import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import com.example.snandmb.R;
import com.example.snandmb.R.layout;
import com.example.snandmb.R.menu;
import com.snmb.ActivityReturnCode;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class TwitterLoginActivity extends Activity {

	public final String conKey="vbsG14ISG49JNs0ux0A2g";
	public final String conKeySecret="lb2Pwr1Xl6E4WNPZIOwwNwrsgXfQRQSS6kylciRyk0";
	public final String accessToken ="111082828-bZHnz1qj2iKYPtGMZNyKHtq3EtaYhjMrafZf1V5b";
	public final String accessTokenSecret ="NJLdDIlgwgBPhqxAiyN59G8dgullbQPZ5hESbahLE";
	private String Tag="SNnMB";
	RequestToken rToken;
	String oauthVerifier;
	CommonsHttpOAuthConsumer consumer;
	DefaultOAuthProvider provider;
	String callbackURL = "callback://tweeter";
	RequestToken requestToken ;
	Twitter t,twitter;
	SharedPreferences sp;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_login);
        StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
		.permitNetwork()
		.build());
		SharedPreferences sp=getSharedPreferences("abc",0);
		if(sp.getBoolean("done", false)==false){
			Editor ed=sp.edit();
			ed.putBoolean("done", true);
			ed.commit();
			Log.d(Tag, "if");
			consumer = new CommonsHttpOAuthConsumer(conKey, conKeySecret);
			provider = new DefaultOAuthProvider("https://twitter.com/oauth/request_token",
					"https://twitter.com/oauth/access_token", "https://twitter.com/oauth/authorize");
			try {
				String oAuthURL = provider.retrieveRequestToken(consumer, callbackURL);
				this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(oAuthURL)));
			} catch (OAuthMessageSignerException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (OAuthNotAuthorizedException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
		StrictMode.setThreadPolicy(old);
    }

    @Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d(Tag, "new");
		sp=getSharedPreferences("snmbData",0);
		StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
		.permitNetwork()
		.build());

		Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith(callbackURL)) {
			String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
			try {
				provider.retrieveAccessToken(consumer, verifier);
				AccessToken accessToken = new AccessToken(consumer.getToken(),consumer.getTokenSecret());
				twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(conKey, conKeySecret);
				twitter.setOAuthAccessToken(accessToken);
				User user = twitter.showUser(accessToken.getUserId());
				if(user.getScreenName()!=null){
					Log.d(Tag, "Got the twitter name:"+user.getScreenName());
					Editor editor= sp.edit();
					editor.putString("twitterusername", user.getScreenName());
					editor.putBoolean("twitterlogin", true);
					editor.commit();
				}				
			}
			catch (Exception e) {
				Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				Log.d(Tag, "Error:"+e.toString());
			}
			StrictMode.setThreadPolicy(old);
		}
		setResult(1);
		finish();
	}
    
}


//sp=getApplicationContext().getSharedPreferences("snmbData",0);
//SharedPreferences.Editor editor= sp.edit();
//editor.putBoolean("twitterlogin", true);
//editor.commit();