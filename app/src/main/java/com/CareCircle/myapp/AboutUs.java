package com.CareCircle.myapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

// Activity class for the "About Us" screen
public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets the layout for this activity to the aboutus.xml file
        setContentView(R.layout.aboutus);
    }
}
