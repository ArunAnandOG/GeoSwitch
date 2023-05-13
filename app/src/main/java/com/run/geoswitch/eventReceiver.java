package com.run.geoswitch;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class eventReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("NAME");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("RUNOPX", "RUNOPX", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "RUNOPX");
        builder.setContentTitle("EVENT");
        builder.setContentText(name);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(true);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "no permissiion", Toast.LENGTH_SHORT).show();
            return;
        }
        managerCompat.notify(2, builder.build());
    }
}