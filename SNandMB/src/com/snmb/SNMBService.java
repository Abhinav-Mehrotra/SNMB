package com.snmb;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;

import com.example.snandmb.R;
import com.getpullsensordata.StartPullSensors;
import com.httpfiles.CheckUpdate;

public class SNMBService extends Service {

	final private String TAG = "SNnMB";
	private NotificationManager nm;
	private AudioManager am;
	final int uniqID=13247;  //required for notifications
	boolean flagR=true;
	String myString = null;
	public static int tabService=0;
	Vibrator vibrator;
	int NOTIFY_ID=0;
	String Oldrid="";
	boolean notificationflag=false;
	int lastlength=0;    

	private static long UPDATE_INTERVAL = 30*1000;  //default
	Context ctx;
	private static Timer timer;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate(){
		super.onCreate();
		timer = new Timer();
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		am= (AudioManager) getSystemService(AUDIO_SERVICE);
		//startSNMBService();
		//callAsynchronousTask();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		callAsynchronousTask();
		return START_STICKY;
	}

//	private void startSNMBService(){ 
//		Log.i(getClass().getSimpleName(), "SNMBService Timer started....");
//		timer.scheduleAtFixedRate(new TimerTask() {
//			public void run() {
//				Log.d(TAG, "checking trigger....");
//				//doServiceWork();
//				startServiceWork();				
//			}
//		}, 1000,UPDATE_INTERVAL);
//	}
	
	
	public void callAsynchronousTask() {
	    final Handler handler = new Handler();
	    Timer timer = new Timer();
	    TimerTask doAsynchronousTask = new TimerTask() {       
	        @Override
	        public void run() {
	            handler.post(new Runnable() {
	                public void run() {       
	                    try {
	                    	StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
	                		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
	                		.permitNetwork()
	                		.build());
	                        startServiceWork();
	                        StrictMode.setThreadPolicy(old);
	                    } catch (Exception e) {
	                        Log.e(TAG, e.toString());
	                    }
	                }
	            });
	        }
	    };
	    timer.schedule(doAsynchronousTask, 0, UPDATE_INTERVAL); 
	}
	

	private void startServiceWork(){
		CheckUpdate checkIt =new CheckUpdate();
		String newUpdate=checkIt.checkNow();
		Log.d(TAG, "Latest update available: "+newUpdate);
		SharedPreferences sp=getSharedPreferences("snmbData",0);
		if(newUpdate.contains("1") && sp.getBoolean("sensing", false)==false){
			Log.d(TAG, "Found a new update");
			Editor ed=sp.edit();
			ed.putBoolean("sensing", true);
			ed.commit();
			startNotifications();
			StartPullSensors sps=new StartPullSensors(getApplicationContext());
			sps.StartSensing();
			//Intent intent = new Intent(getApplicationContext(), com.getpullsensordata.SensorService.class);
			//startService(intent);
		}
	}
	
	private void stopSNMBService(){
		if (timer != null) timer.cancel();
		Log.i(getClass().getSimpleName(), "Timer stopped...");
		SharedPreferences sp=getSharedPreferences("snmbData",0);
		Editor ed=sp.edit();
		ed.putBoolean("sensing", true);
		ed.commit();
	}

	@Override 
	public void onDestroy(){
		super.onDestroy();
		stopSNMBService();
	}

	private void startNotifications(){	
		int icon = R.drawable.image;        // icon from resources
		CharSequence tickerText = "Hello";              // ticker-text
		long when = System.currentTimeMillis();         // notification time
		Context context = getApplicationContext();      // application Context
		CharSequence contentTitle = "My notification";  // message title
		CharSequence contentText = "Hello World!";      // message text
		int maxVol= am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
		am.setRingerMode(2);
		am.setStreamVolume(AudioManager.STREAM_RING, maxVol, 0);

		Intent notificationIntent = new Intent(this, MrJoe.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;

		nm.notify(uniqID,notification);		
	}
}