package com.CareCircle.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    Button checkInBtn, emergencyBtn, careCircleBtn, addContactBtn, mapBtn, historyBtn, aboutBtn, localNumbersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        checkInBtn = findViewById(R.id.AboutusBtn);
        emergencyBtn = findViewById(R.id.emergencyBtn);
        careCircleBtn = findViewById(R.id.careCircleBtn);
        addContactBtn = findViewById(R.id.addContactBtn);
        mapBtn = findViewById(R.id.mapBtn);
        historyBtn = findViewById(R.id.historyBtn);
        aboutBtn = findViewById(R.id.AboutusBtn);
        localNumbersBtn = findViewById(R.id.localNumbersBtn); // <-- add this

        checkInBtn.setOnClickListener(v -> startActivity(new Intent(this, AboutUs.class)));
        emergencyBtn.setOnClickListener(v -> startActivity(new Intent(this, EmergencyAlert.class)));
        careCircleBtn.setOnClickListener(v -> startActivity(new Intent(this, CareCircleList.class)));
        addContactBtn.setOnClickListener(v -> startActivity(new Intent(this, AddContact.class)));
        mapBtn.setOnClickListener(v -> startActivity(new Intent(this, MapScreen.class)));
        historyBtn.setOnClickListener(v -> startActivity(new Intent(this, AlertHistory.class)));
        aboutBtn.setOnClickListener(v -> startActivity(new Intent(this, AboutUs.class)));
        localNumbersBtn.setOnClickListener(v -> startActivity(new Intent(this, LocalEmergencyNumbers.class))); // <-- this too
    }
}
