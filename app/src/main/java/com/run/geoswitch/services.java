package com.run.geoswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import android.widget.Switch;
import android.widget.Toast;

public class services extends AppCompatActivity {

    Switch wifi;
    boolean qq, ww, ee;
    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        wifi = findViewById(R.id.switch1);

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wifi.isChecked()) {
                    editor.putBoolean("wifi", true);
                    editor.commit();
                    Toast.makeText(services.this, "Wifi Checked", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putBoolean("wifi", false);
                    editor.commit();
                    Toast.makeText(services.this, "Wifi Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}