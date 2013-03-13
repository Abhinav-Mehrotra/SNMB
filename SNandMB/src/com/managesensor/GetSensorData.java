package com.managesensor;

/**
 * @author Abhinav Mehrotra
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;

public class GetSensorData {

	int[] sensorList; 

	public GetSensorData(){
		this.sensorList= new AllPullSensors().getIds();
	}

	/**
	 * This method will start sensing for the sensor provided in the constructor.
	 * Data will be copied to a file in Context directory.
	 * @throws ESException 
	 */
	public void getData(){
		new StartSensingData(){	}.execute();
	}

}
