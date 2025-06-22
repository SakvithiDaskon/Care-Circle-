package com.CareCircle.myapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class EmergencyAlert extends AppCompatActivity {

    private Button sendAlertBtn;
    private Database database; // your SQLite helper class
    private static final String EMERGENCY_MESSAGE = "⚠️ Emergency! Please help me. This is an automatic alert from Care Circle.";

    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_alert);

        sendAlertBtn = findViewById(R.id.sendAlertBtn);
        database = new Database(this);

        // Setup permission request callback
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        sendEmergencySMS();
                    } else {
                        Toast.makeText(this, "SMS permission denied. Cannot send alert.", Toast.LENGTH_SHORT).show();
                    }
                });

        sendAlertBtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                sendEmergencySMS();
            } else {
                // Request permission
                requestPermissionLauncher.launch(Manifest.permission.SEND_SMS);
            }
        });
    }

    private void sendEmergencySMS() {
        ArrayList<String> contacts = database.getAllContacts();

        if (contacts.isEmpty()) {
            Toast.makeText(this, "No contacts found. Please add contacts first.", Toast.LENGTH_SHORT).show();
            return;
        }

        SmsManager smsManager = SmsManager.getDefault();
        boolean allSent = true;

        for (String contact : contacts) {
            // Assuming contact format is "Name - PhoneNumber"
            String[] parts = contact.split(" - ");
            if (parts.length == 2) {
                String phoneNumber = parts[1].trim();
                try {
                    smsManager.sendTextMessage(phoneNumber, null, EMERGENCY_MESSAGE, null, null);
                } catch (Exception e) {
                    allSent = false;
                    e.printStackTrace();
                }
            }
        }

        if (allSent) {
            Toast.makeText(this, "Emergency alerts sent to your Care Circle!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Some messages could not be sent. Check your contacts and try again.", Toast.LENGTH_LONG).show();
        }
    }
}
