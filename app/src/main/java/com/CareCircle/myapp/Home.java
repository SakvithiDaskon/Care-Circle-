package com.CareCircle.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    // Buttons for navigation to different screens
    Button checkInBtn, emergencyBtn, careCircleBtn, addContactBtn, mapBtn, historyBtn, aboutBtn, localNumbersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Link buttons to XML views
        checkInBtn = findViewById(R.id.AboutusBtn);
        emergencyBtn = findViewById(R.id.mapBtn);
        careCircleBtn = findViewById(R.id.careCircleBtn);
        addContactBtn = findViewById(R.id.addContactBtn);
        mapBtn = findViewById(R.id.mapBtn);
        historyBtn = findViewById(R.id.historyBtn);
        aboutBtn = findViewById(R.id.AboutusBtn);
        localNumbersBtn = findViewById(R.id.localNumbersBtn); // button for local emergency numbers

        // Set click listeners to open respective activities
        checkInBtn.setOnClickListener(v -> startActivity(new Intent(this, AboutUs.class)));
        careCircleBtn.setOnClickListener(v -> startActivity(new Intent(this, CareCircleList.class)));
        addContactBtn.setOnClickListener(v -> startActivity(new Intent(this, AddContact.class)));
        mapBtn.setOnClickListener(v -> startActivity(new Intent(this, MapScreen.class)));
        historyBtn.setOnClickListener(v -> startActivity(new Intent(this, AlertHistory.class)));
        aboutBtn.setOnClickListener(v -> startActivity(new Intent(this, AboutUs.class)));
        localNumbersBtn.setOnClickListener(v -> startActivity(new Intent(this, LocalEmergencyNumbers.class))); // open emergency numbers
    }
}
