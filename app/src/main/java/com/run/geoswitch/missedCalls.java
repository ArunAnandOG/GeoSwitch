package com.run.geoswitch;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class missedCalls extends BroadcastReceiver {

    String num, old;
    SharedPreferences mPrefs, getmPrefs;
    SharedPreferences.Editor editor;
    int token;
    boolean aBoolean;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        num = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        mPrefs = context.getSharedPreferences("eventtoken", Context.MODE_PRIVATE);
        aBoolean = mPrefs.getBoolean("imp", false);
        if (aBoolean) {
            if (num != null) {
                getmPrefs = context.getSharedPreferences("numbercheck", Context.MODE_PRIVATE);
                editor = getmPrefs.edit();
                old = getmPrefs.getString("runqq", "1");
                if (Long.parseLong(old) != Long.parseLong(num)) {
                    token = 1;
                    editor.putString("runqq", num);
                    editor.putInt("runww", token);
                    editor.commit();
                    Toast.makeText(context, "new number", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "old number", Toast.LENGTH_SHORT).show();
                    token = getmPrefs.getInt("runww", 1);
                    token++;
                    editor.putInt("runww", token);
                    editor.commit();
                    if (token >= 3) {
                        //Toast.makeText(context, "RUN OP", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel("RUNOP", "RUNOP", NotificationManager.IMPORTANCE_HIGH);
                            NotificationManager manager = context.getSystemService(NotificationManager.class);
                            manager.createNotificationChannel(channel);
                        }

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "RUNOP");
                        builder.setContentTitle("IMPORTANT CALL");
                        builder.setContentText(num);
                        builder.setSmallIcon(R.drawable.ic_launcher_background);
                        builder.setAutoCancel(true);
                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(context, "no permission", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        managerCompat.notify(1, builder.build());
                        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            long[] timings = new long[] { 500, 200, 500, 200, 500, 200, 500, 200, 500, 200, 500};
                            int[] amplitudes = new int[] { 200, 0, 200, 0, 200, 0, 200, 0, 200, 0, 200};
                            int repeatIndex = -1;
                            v.vibrate(VibrationEffect.createWaveform(timings, amplitudes, repeatIndex));
                        } else {
                            v.vibrate(500);
                        }
                    }
                    else{
                        Toast.makeText(context, "less than 3 calls", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}