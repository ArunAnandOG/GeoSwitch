package com.run.geoswitch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class time_home extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    Dialog dialog;
    boolean isFromClicked = false;
    EditText editText;


    RecyclerView recyclerView;
    ArrayList<String> item1,item2,item3;
    TimeDatabaseHelper DB;
    TimeAdapter adapter;
    SharedPreferences mPrefs;
    SharedPreferences.Editor editor;
    int mInt,mInt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_home);
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        TextView text = (TextView) findViewById(R.id.textView);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                mPrefs = getSharedPreferences("timetoken", Context.MODE_PRIVATE);
                editor = mPrefs.edit();
                mInt = mPrefs.getInt("runop", 0);
                if(mInt == 0){
                    editor.putInt("runop", 1);
                    editor.commit();
                }
                mInt2 = mInt + 1;
                editor.putInt("runop", mInt2);
                editor.commit();
            }
        });

        DB = new TimeDatabaseHelper(this);
        item1 = new ArrayList<>();
        item2 = new ArrayList<>();
        item3 = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new TimeAdapter(this, item1, item2, item3);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displaydata();
    }

    private void displaydata() {
        Cursor cursor = DB.getListContents();
        if(cursor.getCount()==0)
        {
            Toast.makeText(this, "No Entry Exists", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while(cursor.moveToNext())
            {
                item1.add(cursor.getString(1));
                item2.add(cursor.getString(2));
                item3.add(cursor.getString(3));
            }
        }


        Button silenttimebutton = (Button) dialog.findViewById(R.id.button_from);
        Button unsilenttimebutton = (Button) dialog.findViewById(R.id.button_to);
        Button timesavebutton = (Button) dialog.findViewById(R.id.save_button_time);
        Button timecancelbutton = (Button) dialog.findViewById(R.id.cancel_button_time);
        TextView silenttimetext = (TextView) dialog.findViewById(R.id.time_silent);
        TextView unsilenttimetext = (TextView) dialog.findViewById(R.id.time_unsilent);
        EditText silentname = (EditText) dialog.findViewById(R.id.name_here);

        silenttimebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker1 = new TimePickerFragment();
                timePicker1.show(getSupportFragmentManager(), "Time Picker 1");
                TextView timeedit = (TextView) dialog.findViewById(R.id.time_silent);
                isFromClicked = true;
            }
        });


        unsilenttimebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker2 = new TimePickerFragment();
                timePicker2.show(getSupportFragmentManager(), "Time Picker 2");
                TextView timeedit = (TextView) dialog.findViewById(R.id.time_unsilent);
                isFromClicked = false;

            }
        });
        editText= (EditText) dialog.findViewById(R.id.name_here);

        timesavebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeDatabaseHelper myDB = new TimeDatabaseHelper(time_home.this);
                myDB.addData(silentname.getText().toString().trim(), silenttimetext.getText().toString().trim(), unsilenttimetext.getText().toString().trim(), mInt);
                dialog.dismiss();
                refreshPage();
            }
        });
        timecancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView mainpagetext = (TextView) findViewById(R.id.textView);
                mainpagetext.setText("TIME NOT SET");
                TimeDatabaseHelper myDB = new TimeDatabaseHelper(time_home.this);
                //myDB.deleteDB();
                dialog.dismiss();

            }
        });


    }




    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        String AM_PM = " AM";
        String mm_precede = "";
        int ia = i;

        //24-02-2023


        if (i >= 12) {
            AM_PM = " PM";
            if (i >=13 && i < 24) {
                //i -= 12;
                ia = i - 12;
            }
            else {
                //i = 12;
                ia = 12;
            }
        } else if (i == 0) {
            //i = 12;
            ia = 12;
        }
        if (i1 < 10) {
            mm_precede = "0";
        }
        if ( isFromClicked )
        {
            TextView timeedit = (TextView) dialog.findViewById(R.id.time_silent);
            timeedit.setText(ia+" : "+mm_precede+i1+AM_PM);

            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, i);
            c.set(Calendar.MINUTE, i1);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            startAlarmA(c);

        }
        else
        {
            TextView timeedit = (TextView) dialog.findViewById(R.id.time_unsilent);
            timeedit.setText(ia+" : "+mm_precede+i1+AM_PM);

            Calendar cb = Calendar.getInstance();
            cb.set(Calendar.HOUR_OF_DAY, i);
            cb.set(Calendar.MINUTE, i1);
            cb.set(Calendar.SECOND, 0);
            cb.set(Calendar.MILLISECOND, 0);
            startAlarmB(cb);

        }
    }

    private void startAlarmA(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, SilentReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, mInt, intent, PendingIntent.FLAG_MUTABLE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void startAlarmB(Calendar cb){
        AlarmManager alarmManagerb = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intentb = new Intent(this, UnsilentReceiver.class);
        PendingIntent pendingIntentb = PendingIntent.getBroadcast(this, mInt, intentb, PendingIntent.FLAG_MUTABLE);
        alarmManagerb.setExact(AlarmManager.RTC_WAKEUP, cb.getTimeInMillis(), pendingIntentb);
    }

    public void refreshPage(){
        finish();
        startActivity(getIntent());
    }
}