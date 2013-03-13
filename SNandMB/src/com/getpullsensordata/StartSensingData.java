package com.getpullsensordata;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;

/**
 * This class will initiate a thread which will start sensing the sensor provided in the constructor.
 * Sensor data can be retrieve by overriding onPostExecute method
 * @author Abhinav
 *
 */

public class StartSensingData extends AsyncTask<Void, Void, SensorData>
{
	private final ESSensorManager sensorManager;
	private final int sensorType;
	private SensorDataListener listener;


	public StartSensingData(int sensorType, Context context) throws ESException
	{
		this.sensorType = sensorType;
		sensorManager = ESSensorManager.getSensorManager(context);
		listener=new SensorDataListener() {
			public void onDataSensed(SensorData arg0) {
				// TODO Auto-generated method stub
				System.out.print(arg0);
			}
			public void onCrossingLowBatteryThreshold(boolean arg0) {
				// TODO Auto-generated method stub
			}
		};
	}

	protected void onPreExecute(Void unused) {
		try {
			System.out.print("Started sensing");
			sensorManager.subscribeToSensorData(sensorType, listener);
		} catch (ESException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected SensorData doInBackground(Void... params)
	{
		try
		{
			Log.d("Sensor Task", "Sampling from Sensor");
			return sensorManager.getDataFromSensor(sensorType);
		}
		catch (ESException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	protected void onPostExecute(Void unused) {
		try {
			sensorManager.unsubscribeFromSensorData(sensorType);
		} catch (ESException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}

