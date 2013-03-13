package com.managesensor;

import com.example.snandmb.R;
import com.snmb.MrJoe;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SensorService extends Service {

	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(getClass().getSimpleName(), "SensorService created...");
		//new StartSensingData().execute();
	}		
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i(getClass().getSimpleName(), "SensorService started...");
//		new StartSensingData().execute(getApplicationContext());
		return super.onStartCommand(intent, flags, startId);
	}
}
