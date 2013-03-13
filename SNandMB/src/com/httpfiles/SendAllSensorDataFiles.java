package com.httpfiles;

import android.content.Context;

import com.managesensor.AllPullSensors;

public class SendAllSensorDataFiles {

	int arr[];
	Context context;
	
	public SendAllSensorDataFiles() {
		AllPullSensors aps = new AllPullSensors();
		this.arr=aps.getIds();
	}
	
	public void sendAllFiles(){
		for(int i=0;i<arr.length;i++){
			new SendOneSensorDataFile(arr[i],context){}.execute();
		}
	}
}
