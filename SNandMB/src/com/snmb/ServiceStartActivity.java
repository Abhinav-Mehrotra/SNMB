package com.snmb;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.snandmb.R;

public class ServiceStartActivity extends Activity {

	public static final String SERVICE_CLASSNAME = "com.snmb.SNMBService";
    private Button button;
    final private String TAG = "SNnMB";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_start);
        button = (Button) findViewById(R.id.srvcBtn);
        updateButton();
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateButton();
	}

	/*public void startMyService(View v){
		Intent intent = new Intent(this, TriggerService.class);
		startService(intent);
	}*/

	private void updateButton() {
		if (serviceIsRunning()) {
			button.setText("Stop Service");
			button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					button.setText("Start Service");
					stopMyService();
					updateButton();
				}
			});

		} else {
			button.setText("Start Service");
			button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					button.setText("Stop Service");
					startMyService();
					updateButton();
				}
			});
		}
	}

	private void startMyService() {
		Toast.makeText(getApplicationContext(), "Service Start", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(getApplicationContext(), SNMBService.class);
		startService(intent);
	}

	private void stopMyService() {
		Toast.makeText(getApplicationContext(), "Service Stoped", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(getApplicationContext(), SNMBService.class);
		stopService(intent);
	}

	private boolean serviceIsRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (SERVICE_CLASSNAME.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
  
    
    public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		SharedPreferences sp= getSharedPreferences("snmbData", 0);
    	SharedPreferences.Editor ed =sp.edit();
    	ed.putBoolean("backKey", true);
    	ed.commit();
	}
}
