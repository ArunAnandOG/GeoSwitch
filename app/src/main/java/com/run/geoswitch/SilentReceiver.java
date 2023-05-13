package com.run.geoswitch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class SilentReceiver extends BroadcastReceiver {

    Boolean wifi;
    Boolean data;
    Boolean bt;
    WifiManager wifiManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager ma;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        ma = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if(ma.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
            ma.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }

// Wait for 1 second
// Direct changing won't set the Do Not Disturb on
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ma.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        //WIFI
        wifi = sharedPreferences.getBoolean("wifi", false);
        if(wifi){
            if(wifiManager.isWifiEnabled()){
                wifiManager.setWifiEnabled(false);
            }
        }

        Toast.makeText(context, "SILENT DONE", Toast.LENGTH_LONG).show();
    }
}