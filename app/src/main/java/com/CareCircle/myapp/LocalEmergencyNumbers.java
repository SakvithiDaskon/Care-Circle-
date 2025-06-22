package com.CareCircle.myapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LocalEmergencyNumbers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_emergency_numbers);


        TextView police = findViewById(R.id.police);
        TextView fire = findViewById(R.id.fire);
        TextView ambulance = findViewById(R.id.ambulance);


        police.setOnClickListener(v -> dialNumber("119"));
        fire.setOnClickListener(v -> dialNumber("110"));
        ambulance.setOnClickListener(v -> dialNumber("1990"));
    }


    private void dialNumber(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }
}
