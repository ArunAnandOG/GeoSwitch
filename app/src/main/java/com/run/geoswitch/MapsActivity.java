package com.run.geoswitch;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.run.geoswitch.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    String radius, name;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;
    private float GEOFENCE_RADIUS = 200;
    private String GEOFENCE_ID = "RUN";
    private static final String TAG = "MapsActivity";
    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    int mInt, mInt2;
    SharedPreferences mPrefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(11.0508, 76.0714);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

        enableUserLocation();

        mMap.setOnMapLongClickListener(this);
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        if (Build.VERSION.SDK_INT >= 29) {
            //We need background permission
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                handleMapLongClick(latLng);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    //We show a dialog and ask for permission
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                }
            }

        } else {
            handleMapLongClick(latLng);
        }

    }

    private void handleMapLongClick(LatLng latLng) {
        mMap.clear();
        //geofencingClient.removeGeofences(geofenceHelper.getPendingIntent());
        addMarker(latLng);

        Bundle bundle = getIntent().getExtras();
        radius = bundle.getString("radius");
        name = bundle.getString("name");
        int radiusxx = Integer.parseInt(radius);
        if (radiusxx < 0) {
            radiusxx = 1;
        }
        Toast.makeText(this, "" + latLng, Toast.LENGTH_LONG).show();
        addCircle(latLng, radiusxx);
        addGeofence(latLng, radiusxx, name);
        MapDatabaseHelper myDB = new MapDatabaseHelper(MapsActivity.this);
        mPrefs = getSharedPreferences("maptoken", Context.MODE_PRIVATE);
        editor = mPrefs.edit();
        mInt = mPrefs.getInt("runopx", 0);
        if (mInt == 0) {
            editor.putInt("runopx", 1);
            editor.commit();
        }
        mInt2 = mInt + 1;
        editor.putInt("runopx", mInt2);
        editor.commit();
        myDB.addData(name, mInt, latLng.toString(), radiusxx);
        Intent intent = new Intent(MapsActivity.this, MainActivity.class);
        startActivity(intent);

    }


    private void addGeofence(LatLng latLng, float radius, String name) {

        Geofence geofence = geofenceHelper.getGeofence(name, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, mInt, intent, PendingIntent.FLAG_MUTABLE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    private void addMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        mMap.addMarker(markerOptions);
    }

    private void addCircle(LatLng latLng, float radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
        circleOptions.fillColor(Color.argb(60, 255, 0, 0));
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);

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
                        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                        Toast.makeText(MapsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
    public void runopinchat(){
        Toast.makeText(this, "helow", Toast.LENGTH_SHORT).show();
    }
}