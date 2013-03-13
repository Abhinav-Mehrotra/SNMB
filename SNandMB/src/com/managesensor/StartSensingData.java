package com.managesensor;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.data.SensorData;

/**
 * This class will initiate a thread which will start sensing the sensor provided in the constructor.
 * Sensor data can be retrieve by overriding onPostExecute method
 * @author Abhinav
 *
 */

public class StartSensingData extends AsyncTask<Context, Void, ArrayList<SensorData>>{
	//private ESSensorManager sensorManager;
	private int[] sensorList;
	private ArrayList<SensorData> sensorDataArray;	

	@Override
	protected void onPreExecute() {
		Log.d(getClass().toString(), "Sensing started");
	}

	@Override
	protected ArrayList<SensorData> doInBackground(Context... params) {		
		sensorList = new AllPullSensors().getIds();	
		sensorDataArray = new ArrayList<SensorData>();
		Context context = params[0];
		for (int sensorID : sensorList) {
			Log.d(getClass().toString(), "Sensing from "+sensorID);
			try {
				ESSensorManager sensorManager = ESSensorManager.getSensorManager(context);
				sensorDataArray.add(sensorManager.getDataFromSensor(sensorID));
			} catch (ESException e) {
				Log.d(getClass().toString(), "Error in getting sensor data!! \n"+e.toString());
				e.printStackTrace();
			}				
		}		
		return sensorDataArray;
	}
	
	@Override
	public void onPostExecute(ArrayList<SensorData> data){
		try {
			File root = new File(Environment.getExternalStorageDirectory(), "SensorData");
            if (!root.exists()){
                root.mkdirs();
            }            
			File file = new File(root, "ContextData.txt");
			//FileOutputStream os = new FileOutputStream(file, true);  It can be used if we want to append
			FileOutputStream os = new FileOutputStream(file);
			OutputStreamWriter out = new OutputStreamWriter(os);
			if (data != null){
//				JSONFormatter formatter = DataFormatter.getJSONFormatter(sensorType);
//				out.write(formatter.toJSON(data).toJSONString());				
				for (int i=0; i<sensorList.length; i++){
					int sensorType = sensorList[i];
					SensorData sensorData = data.get(i);
					JSONFormatter formatter = DataFormatter.getJSONFormatter(sensorType);
					Log.d(getClass().toString(), "Sensed "+formatter.toJSON(sensorData).toJSONString());
					out.write(formatter.toJSON(sensorData).toJSONString());
				}
			}
			else{
				out.write("Null (e.g., sensor off)");
			}
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.print(e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.print(e.toString());
		} 
		System.out.print("Stopped sensing");
	}

}

