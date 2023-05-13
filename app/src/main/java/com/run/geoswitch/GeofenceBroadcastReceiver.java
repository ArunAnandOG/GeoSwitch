package com.run.geoswitch;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    Boolean wifi;
    Boolean data;
    Boolean bt;
    AudioManager myAudioManager;
    WifiManager wifiManager;
    BluetoothAdapter mBluetoothAdapter;
    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);


        myAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (geofencingEvent != null) {
            int transitionType = geofencingEvent.getGeofenceTransition();
            switch (transitionType) {
                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    //SILENT
                    if(myAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

                    //WIFI
                    wifi = sharedPreferences.getBoolean("wifi", false);
                    if(wifi){
                        if(wifiManager.isWifiEnabled()){
                            wifiManager.setWifiEnabled(false);
                        }
                    }
                    Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show();
                    break;

                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    //WIFI
                    wifi = sharedPreferences.getBoolean("wifi", false);
                    if(wifi){
                        wifiManager.setWifiEnabled(true);
                    }
                    Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
    }
}