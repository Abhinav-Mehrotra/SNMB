package com.managesensor;

import com.ubhave.sensormanager.ESException;

import android.content.Context;
import android.util.Log;

public class SensorInitiator {

	private Context context;
	
	public SensorInitiator(Context context) {
		this.context=context;
	}

	public void startSensingAll(){		
		GetSensorData gd=new GetSensorData();
		gd.getData();
	}

}
