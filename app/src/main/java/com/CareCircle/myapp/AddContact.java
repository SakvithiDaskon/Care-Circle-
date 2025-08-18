package com.CareCircle.myapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddContact extends AppCompatActivity {
    EditText nameInput, phoneInput;
    Button saveBtn;
    Database db;  // database helper for storing contacts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);

        // link UI elements
        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        saveBtn = findViewById(R.id.saveContactBtn);

        db = new Database(this);  // initialize database helper

        saveBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();

            // check empty inputs
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
                Toast.makeText(this, "Enter name and phone", Toast.LENGTH_SHORT).show();
                return;
            }

            // validate name (letters and spaces only)
            if (!name.matches("[a-zA-Z ]+")) {
                Toast.makeText(this, "Name can only contain letters and spaces", Toast.LENGTH_SHORT).show();
                return;
            }

            // validate phone (exactly 10 digits)
            if (!phone.matches("\\d{10}")) {
                Toast.makeText(this, "Phone number must contain exactly 10 digits", Toast.LENGTH_SHORT).show();
                return;
            }

            // insert into database
            boolean inserted = db.insertContact(name, phone);
            if (inserted) {
                Toast.makeText(this, "Contact Saved", Toast.LENGTH_SHORT).show();
                finish();  // go back to previous screen
            } else {
                Toast.makeText(this, "Error saving contact", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
