package com.getpullsensordata;

/**
 * @author Abhinav Mehrotra
 * 
 */


public class AllPullSensors {

	public final static int SENSOR_TYPE_ACCELEROMETER = 5001;
	public final static int SENSOR_TYPE_BLUETOOTH = 5003;
	public final static int SENSOR_TYPE_LOCATION = 5004;
	public final static int SENSOR_TYPE_MICROPHONE = 5005;
	public final static int SENSOR_TYPE_WIFI = 5010;
	
	/**
	 * This method assigns the ids of all pull sensors in an array and return it
	 * @return int[] Array of sensor ids
	 */
	public int[] getIds(){
		int ar[]={SENSOR_TYPE_ACCELEROMETER,SENSOR_TYPE_BLUETOOTH,SENSOR_TYPE_LOCATION,SENSOR_TYPE_MICROPHONE,SENSOR_TYPE_WIFI};
		return ar;
	}

}
