package com.getpullsensordata;

import android.content.Context;

public class SensorInitiator {

	private Context context;
	
	public SensorInitiator(Context context) {
		this.context=context;
	}

	public void startSensingAll(){
		AllPullSensors aps=new AllPullSensors();
		int ar[]=aps.getIds();
		for(int i=0;i<5;i++){
			GetSensorData gd=new GetSensorData(ar[i], context);
			gd.getData();
		}
	}

}
