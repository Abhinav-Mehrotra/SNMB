package com.getpullsensordata;

/**
 * @author Abhinav Mehrotra
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.os.Environment;

import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.dataformatter.json.JSONFormatter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;

public class GetSensorData {

	int sensorType;
	Context context;

	public GetSensorData(int sensorType, Context context){
		this.sensorType=sensorType;
		this.context=context;
	}

	/**
	 * This method will start sensing for the sensor provided in the constructor.
	 * Data will be copied to a file in Context directory.
	 */
	public void getData(){
		try {
			new StartSensingData(sensorType,context)
			{
				@Override
				public void onPostExecute(SensorData data)
				{

					try {
						File root = new File(Environment.getExternalStorageDirectory(), "SensorData");

			            if (!root.exists()) {
			                root.mkdirs();

			            }
			            
						File file = new File(root, sensorType+".txt");
						//FileOutputStream os = new FileOutputStream(file, true);  It can be used if we want to append
						FileOutputStream os = new FileOutputStream(file);
						OutputStreamWriter out = new OutputStreamWriter(os);
						if (data != null)
						{
							JSONFormatter formatter = DataFormatter.getJSONFormatter(sensorType);
							out.write(formatter.toJSON(data).toJSONString());

						}
						else {
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

					//setSensorStatusField(UNSUBSCRIBED);
					System.out.print("Stopped sensing");
				}

			}.execute();


		} catch (ESException e) {
			// TODO Auto-generated catch block
			System.out.print(e.toString());
		}
	}

}
