package com.CareCircle.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    EditText username, password;
    Button loginBtn, signupBtn;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);

        db = new Database(this); // Initialize database helper

        loginBtn.setOnClickListener(view -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if user exists
            boolean isValid = db.checkUser(user, pass);

            if (isValid) {
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();

                // 👉 Save check-in login time
                db.insertCheckIn(user);

                // 👉 Pass username to next activity if needed
                Intent intent = new Intent(this, Home.class);
                intent.putExtra("username", user);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });

        signupBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, Signup.class));
        });
    }
}
