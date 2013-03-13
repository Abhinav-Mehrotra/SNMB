package com.httpfiles;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

public class SendOneSensorDataFile extends AsyncTask<String,Void,String>{
	final private String TAG = "SNnMB";
	HttpURLConnection connection = null;
	DataOutputStream outputStream = null;
	DataInputStream inputStream = null;
	String pathToOurFile = "/mnt/sdcard/";
	String urlServer = "http://studentweb.cs.bham.ac.uk/~axm514/getsensordata.php"; 
	//"http://studentweb.cs.bham.ac.uk/~axm514/getsensordata.php"; 
	//"http://abhinavtest.net76.net/servetut/testingr.php";
	String lineEnd = "\r\n";
	String twoHyphens = "--";
	String boundary =  "*****";
	int bytesRead, bytesAvailable, bufferSize;
	byte[] buffer;
	int maxBufferSize = 1*1024*1024;
	String serverResponseMessage;
	int fileName;
	Context context;

	public SendOneSensorDataFile(int fileName, Context context){
		this.fileName=fileName;
		this.pathToOurFile=this.pathToOurFile+fileName+".txt";
		this.context=context;
	}
	
	public SendOneSensorDataFile(String folderName, String fileName, Context context){
		this.pathToOurFile=this.pathToOurFile+folderName+"/"+fileName;
		this.context=context;
	}

	@Override
	public void onPreExecute(){
		Log.d(TAG, "Started Sending File");
		Log.d(TAG, "File Name:"+ pathToOurFile);
		Log.d(TAG, "Server Name:"+ urlServer);
	}

	@Override
	protected String doInBackground(String... arg0) {
		try{
			Log.d(TAG, "trying Sending File");
			FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );
			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();
			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			// Enable POST method
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
			outputStream = new DataOutputStream( connection.getOutputStream() );
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0)
			{
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// Responses from the server (code and message)
			int serverResponseCode = connection.getResponseCode();
			serverResponseMessage = connection.getResponseMessage();
			Log.e("response",""+serverResponseCode);
			Log.e("serverResponseMessage",""+serverResponseMessage);
			System.out.println(serverResponseCode);
			System.out.println(serverResponseMessage);
			fileInputStream.close();
			outputStream.flush();
			outputStream.close();
			serverResponseMessage+="\n and try end";
		}
		catch (Exception ex)
		{
			Log.e(TAG, ex.toString());
			serverResponseMessage=ex.toString();

		}
		return ("Bytes read: "+serverResponseMessage);
	}
	@Override
	protected void onPostExecute(String result){
		Log.d(TAG, "File Sending Complete");
		Log.d(TAG, result);
		File file = new File(pathToOurFile);
		boolean deleted = file.delete();
		Log.d(TAG, "File Deleted:" + deleted);
		SharedPreferences sp=context.getSharedPreferences("snmbData",0);
		Editor ed=sp.edit();
		ed.putBoolean("sensing", false);
		ed.commit();
	}

}
