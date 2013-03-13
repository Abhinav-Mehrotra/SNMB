package com.httpfiles;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

public class CheckUpdate {
	
	public String checkNow(){
		String myString="0";
		try{
            URL myURL = new URL("http://studentweb.cs.bham.ac.uk/~axm514/checktrigger1.php");   
            URLConnection ucon = myURL.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while((current=bis.read())!=-1){
                baf.append((byte)current);
            }
            myString = new String (baf.toByteArray());
            return myString;
        }catch(Exception e){
    		Log.i(getClass().getSimpleName(), "Error while checking for update!!\n"+e.toString());
        }
		return myString;
	}
}
