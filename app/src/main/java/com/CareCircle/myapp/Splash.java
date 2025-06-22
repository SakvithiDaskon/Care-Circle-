package com.CareCircle.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_CareCircle); // Set light blue theme
        setContentView(R.layout.splash); // using splash.xml

        // Delay and navigate to Login
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Splash.this, com.CareCircle.myapp.Login.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}
