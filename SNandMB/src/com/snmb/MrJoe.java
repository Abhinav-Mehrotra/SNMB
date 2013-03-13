package com.snmb;

import com.example.snandmb.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MrJoe extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mr_joe);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_mr_joe, menu);
        return true;
    }
}
