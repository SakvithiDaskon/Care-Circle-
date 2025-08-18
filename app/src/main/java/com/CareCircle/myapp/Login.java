package com.CareCircle.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class Login extends AppCompatActivity {

    EditText username, password;
    Button loginBtn, signupBtn;
    Database db; // database helper
    SharedPreferences prefs; // to store biometric settings

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // link UI elements
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);

        db = new Database(this);
        prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        boolean biometricEnabled = prefs.getBoolean("biometric_enabled", false);
        String savedUser = prefs.getString("biometric_username", null);

        // show biometric prompt if enabled
        if (biometricEnabled && savedUser != null) {
            showBiometricPrompt(savedUser);
        }

        loginBtn.setOnClickListener(view -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            // check empty inputs
            if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isValid = db.checkUser(user, pass);

            if (isValid) {
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
                db.insertCheckIn(user); // log check-in

                // enable biometric login for next time if not enabled
                if (!prefs.getBoolean("biometric_enabled", false)) {
                    prefs.edit()
                            .putBoolean("biometric_enabled", true)
                            .putString("biometric_username", user)
                            .apply();
                    Toast.makeText(this, "Fingerprint login enabled for next time", Toast.LENGTH_SHORT).show();
                }

                goToHome(user);
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });

        signupBtn.setOnClickListener(view -> startActivity(new Intent(this, Signup.class)));
    }

    // Show biometric fingerprint authentication prompt
    private void showBiometricPrompt(String savedUser) {
        BiometricManager biometricManager = BiometricManager.from(this);
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                != BiometricManager.BIOMETRIC_SUCCESS) {
            return;
        }

        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(Login.this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Toast.makeText(Login.this, "Biometric Auth Success", Toast.LENGTH_SHORT).show();
                        db.insertCheckIn(savedUser); // log check-in
                        goToHome(savedUser);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(Login.this, "Biometric Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login with Fingerprint")
                .setDescription("Use your fingerprint to login")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    // Navigate to Home activity after login
    private void goToHome(String username) {
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}
