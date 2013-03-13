package com.getpullsensordata;

import android.content.Context;
import android.util.Log;

import com.ubhave.sensormanager.ESException;

public class StartPullSensors {

	int[] SensorIds;
	private final Context context;
	final private String TAG = "SNnMB";
	public StartPullSensors(Context context){
		AllPullSensors aps=new AllPullSensors();
		this.SensorIds=aps.getIds();
		this.context=context;
	}
	
	public void StartSensing(){
		//for(int i:SensorIds){
		int i=0;
			try {
				new SampleOnceTask(i,context).execute();
			} catch (ESException e) {
				Log.e(TAG, e.toString());
			}
		//}
	}
}
