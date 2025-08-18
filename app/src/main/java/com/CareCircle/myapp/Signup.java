package com.CareCircle.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Signup extends AppCompatActivity {

    EditText username, email, password, confirmPassword;
    Button signupBtn;
    Database db; // database helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // link UI elements
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signupBtn = findViewById(R.id.signupBtn);

        db = new Database(this);  // initialize database

        signupBtn.setOnClickListener(view -> {
            String user = username.getText().toString().trim();
            String mail = email.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String confirmPass = confirmPassword.getText().toString().trim();

            // validate inputs
            if (TextUtils.isEmpty(user) || TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirmPass)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!pass.equals(confirmPass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                // insert new user in database
                boolean inserted = db.insertUser(user, mail, pass);
                if (inserted) {
                    Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, Login.class)); // go to login screen
                    finish();
                } else {
                    Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
