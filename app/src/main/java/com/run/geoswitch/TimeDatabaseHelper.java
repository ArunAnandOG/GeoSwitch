package com.run.geoswitch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class TimeDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String DATABASE_NAME = "mylist.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "mytimelist_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "ITEM1";
    public static final String COL3 = "ITEM2";
    public static final String COL4 = "ITEM3";
    public static final String COL5 = "ITEM4";




    public TimeDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " ITEM1 TEXT, ITEM2 TEXT, ITEM3 TEXT, ITEM4 INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int ii) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void deleteDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + TABLE_NAME);
        onCreate(db);
    }
    public void addData(String item1, String item2, String item3, int item4) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues run = new ContentValues();
        run.put(COL2, item1);
        run.put(COL3, item2);
        run.put(COL4, item3);
        run.put(COL5, item4);

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
