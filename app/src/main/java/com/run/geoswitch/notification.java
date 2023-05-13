package com.run.geoswitch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class notification extends AppCompatActivity {


    Calendar calendar;
    int yearXX, monthXX, dayXX;
    Dialog dialog;
    RecyclerView recyclerView;
    Switch imp;
    FloatingActionButton floatingActionButton;
    TextView dateshow;
    Button choosedate,savedate;
    EditText datename;
    DatePickerDialog datePickerDialog;
    AlarmManager manager;
    Intent myIntent;
    PendingIntent pendingIntent;
    SharedPreferences mPrefs;
    SharedPreferences.Editor editor;
    int mInt,mInt2;
    eventDatabaseHelper DB;
    ArrayList<String> item1,item3;
    EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        imp=findViewById(R.id.switch4);
        floatingActionButton=findViewById(R.id.floatingActionButton3);
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup4);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Bundle bundle = new Bundle();
        dateshow=dialog.findViewById(R.id.event_text);
        choosedate=dialog.findViewById(R.id.event_button);
        savedate=dialog.findViewById(R.id.eventSave);
        datename=dialog.findViewById(R.id.event_name);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        choosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(notification.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dayXX = dayOfMonth;
                                monthXX = monthOfYear;//add +1
                                yearXX = year;

                                dateshow.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        savedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qqqq=datename.getText().toString();
                myIntent = new Intent(notification.this,eventReceiver.class);
                myIntent.putExtra("NAME", qqqq);

                mPrefs = getSharedPreferences("eventtoken", Context.MODE_PRIVATE);
                editor = mPrefs.edit();
                mInt = mPrefs.getInt("event", 0);
                if(mInt == 0){
                    editor.putInt("event", 1);
                    editor.commit();
                }
                mInt2 = mInt + 1;
                editor.putInt("event", mInt2);
                editor.commit();

                pendingIntent = PendingIntent.getBroadcast(notification.this, mInt, myIntent, PendingIntent.FLAG_MUTABLE);
                Calendar calendarToSchedule = Calendar.getInstance();
                calendarToSchedule.setTimeInMillis(System.currentTimeMillis());
                calendarToSchedule.clear();
                //.Set(Year, Month, Day, Hour, Minutes, Seconds);
                calendarToSchedule.set(yearXX, monthXX, dayXX, 0, 0, 0);
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarToSchedule.getTimeInMillis(), pendingIntent);
                eventDatabaseHelper myDB = new eventDatabaseHelper(notification.this);
                myDB.addData(datename.getText().toString(), mInt, dateshow.getText().toString().trim());
                refreshPage();
                dialog.dismiss();
            }
        });
        imp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imp.isChecked()) {
                    mPrefs = getSharedPreferences("eventtoken", Context.MODE_PRIVATE);
                    editor = mPrefs.edit();
                    editor.putBoolean("imp", true);
                    editor.commit();
                    Toast.makeText(notification.this, "IMPORTANT CALLS TOGGLED", Toast.LENGTH_SHORT).show();
                } else {
                    mPrefs = getSharedPreferences("eventtoken", Context.MODE_PRIVATE);
                    editor = mPrefs.edit();
                    editor.putBoolean("imp", false);
                    editor.commit();
                }
            }
        });

        DB = new eventDatabaseHelper(this);
        item1 = new ArrayList<>();
        item3 = new ArrayList<>();

        recyclerView = findViewById(R.id.notificationView);
        adapter = new EventAdapter(this, item1 , item3);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displaydata();
    }

    private void displaydata() {
        Cursor cursor = DB.getListContents();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Entry Exists", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                item1.add(cursor.getString(1));
                item3.add("" + cursor.getString(3));
            }
        }
        cursor.close();
    }
    public void refreshPage(){
        finish();
        startActivity(getIntent());
    }
}