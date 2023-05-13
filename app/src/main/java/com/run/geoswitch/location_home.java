package com.run.geoswitch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class location_home extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    Dialog dialog,dialogx;
    Button buttonadd,editR;
    EditText name,radius,radiusEdit;
    MapDatabaseHelper DB;
    ArrayList<String> item1,item4;
    MapAdapter adapter;
    int newR;
    SQLiteDatabase dbx;
    private GeofencingClient geofencingClient;
    private static final String TAG = "location_home";
    private GeofenceHelper geofenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_home);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton2);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup2);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Bundle bundle = new Bundle();

        buttonadd=(Button) dialog.findViewById(R.id.add_button_location);
        name=(EditText) dialog.findViewById(R.id.location_name);
        radius=(EditText) dialog.findViewById(R.id.location_radius);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        buttonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String radiusx=radius.getText().toString();
                String namex=name.getText().toString();
                Intent intentx=new Intent(location_home.this, MapsActivity.class);

                bundle.putString("radius", radiusx);
                bundle.putString("name", namex);
                intentx.putExtras(bundle);
                startActivity(intentx);
            }
        });
        DB = new MapDatabaseHelper(this);
        item1 = new ArrayList<>();
        item4 = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerview);
        adapter = new MapAdapter(this, item1 , item4);
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
                item4.add("RADIUS : " + cursor.getString(4) + "M");
            }
        }
        cursor.close();
    }

    public void editRadius(String hhh){
        dialogx = new Dialog(this);
        dialogx.setContentView(R.layout.popup3);
        dialogx.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogx.show();
        editR = dialogx.findViewById(R.id.editButtonMap);
        radiusEdit = dialogx.findViewById(R.id.location_radiusEdit);
        editR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newR = Integer.parseInt(radiusEdit.getText().toString());
                dialogx.dismiss();
                Toast.makeText(location_home.this, ""+hhh, Toast.LENGTH_SHORT).show();
                dbx = DB.getWritableDatabase();
                ContentValues newValues = new ContentValues();
                newValues.put("ITEM4", newR);
                dbx.update(MapDatabaseHelper.TABLE_NAME, newValues,MapDatabaseHelper.COL2 + " = " + "\"" + hhh + "\"", null);
                //dbx.query("UPDATE "+ MapDatabaseHelper.TABLE_NAME + " SET ITEM4 = " + newR + " where " + MapDatabaseHelper.COL2 + " = " + "\"" + hhh + "\";", null);
                refreshPage();
                Cursor dataxx = dbx.rawQuery("SELECT * FROM " + MapDatabaseHelper.TABLE_NAME + " where " + MapDatabaseHelper.COL2 + " = " + "\"" + hhh + "\" ;", null);
                dataxx.moveToFirst();
                int qqq= Integer.parseInt(dataxx.getString(4));//radius
                int www= Integer.parseInt(dataxx.getString(2));//token
                String eee= dataxx.getString(1);//name
                String rrr= dataxx.getString(3);//latlng
                dataxx.close();
                String ppp = rrr.replaceAll("[()latng/:]","");
                String[] ttt= ppp.split(",");
                Double lat = Double.parseDouble(ttt[0]);
                Double lng = Double.parseDouble(ttt[1]);
                LatLng latLng = new LatLng(lat,lng);
                //Toast.makeText(location_home.this, ""+latLng.toString(), Toast.LENGTH_SHORT).show();
                removeGeo(newR, www, latLng, eee);
            }
        });
    }

    public void removeGeo(int radius, int token, LatLng latLng, String name) {
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, token, intent, PendingIntent.FLAG_MUTABLE);
        geofencingClient.removeGeofences(pendingIntent)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Geofence geofence = geofenceHelper.getGeofence(name, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);
                        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
                        if (ActivityCompat.checkSelfPermission(location_home.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: Geofence Added...");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        String errorMessage = geofenceHelper.getErrorString(e);
                                        Log.d(TAG, "onFailure: " + errorMessage);
                                    }
                                });
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(location_home.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void refreshPage(){
        finish();
        startActivity(getIntent());
    }

}