package com.CareCircle.myapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapScreen extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Database database; // database for contacts
    private static final int PERMISSION_REQUEST_CODE = 101;
    private Button btnSendEmergency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_screen);

        database = new Database(this); // Initialize database
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize Google Map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnSendEmergency = findViewById(R.id.btnSendEmergency);
        btnSendEmergency.setOnClickListener(v -> sendEmergencySms()); // send SMS to contacts

        checkAndRequestPermissions(); // request necessary permissions
    }

    // Request location and SMS permissions if not granted
    private void checkAndRequestPermissions() {
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[0]),
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Enable user location if permission granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            // Get last known location and show marker
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(currentLatLng).title("You are here"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                } else {
                    // Default to Colombo if location unavailable
                    LatLng colombo = new LatLng(6.9271, 79.8612);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(colombo, 12));
                }
            });
        }
    }

    // Send emergency SMS with current location to all contacts
    private void sendEmergencySms() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
            checkAndRequestPermissions();
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                String message = "EMERGENCY! Please help me. This is an automatic alert from Care Circle. My location: https://maps.google.com/?q=" + lat + "," + lon;

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    Cursor cursor = database.getAllContactsCursor();

                    // send SMS to each contact
                    if (cursor.moveToFirst()) {
                        do {
                            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone")).replaceAll("\\s+", "");
                            smsManager.sendTextMessage(phone, null, message, null, null);
                        } while (cursor.moveToNext());
                    }
                    cursor.close();

                    Toast.makeText(this, "Emergency SMS sent to Care Circle contacts!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "SMS sending failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Handle permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (!allGranted) {
                Toast.makeText(this, "Permissions are required for this app", Toast.LENGTH_SHORT).show();
            } else if (mMap != null) {
                onMapReady(mMap); // refresh map if permissions granted
            }
        }
    }
}
