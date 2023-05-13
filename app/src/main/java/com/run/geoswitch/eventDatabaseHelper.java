package com.run.geoswitch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class eventDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "mylist3.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "eventlist_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "ITEM1";//name
    public static final String COL3 = "ITEM2";//token
    public static final String COL4 = "ITEM3";//date

    public eventDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " ITEM1 TEXT , ITEM2 INTEGER , ITEM3 TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void addData(String item1, int item2, String item3) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues run = new ContentValues();
        run.put(COL2, item1);//name
        run.put(COL3, item2);//token
        run.put(COL4, item3);//date

        long result = db.insert(TABLE_NAME, null, run);

        if (result == -1) {
            Toast.makeText(context, "failed",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "SUCCESS",Toast.LENGTH_SHORT).show();
        }
    }
    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
}
