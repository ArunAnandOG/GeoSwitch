package com.run.geoswitch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class UnsilentReceiver extends BroadcastReceiver {

    Boolean wifi;
    Boolean data;
    Boolean bt;
    WifiManager wifiManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager myAudioManager;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        myAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        //WIFI
        wifi = sharedPreferences.getBoolean("wifi", false);
        if(wifi){
            if(wifiManager.isWifiEnabled()){
                wifiManager.setWifiEnabled(false);
            }
        }
        Toast.makeText(context, "UNSILENT DONE", Toast.LENGTH_LONG).show();
    }
}