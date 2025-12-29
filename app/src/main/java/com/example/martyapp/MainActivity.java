package com.example.martyapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText;
    private CheckBox termsCheckBox;
    private Button registerButton;
    private ProgressBar pb_register;
    private TextView loginTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize FirebaseAuth and DatabaseReference
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#0F3E4D")); // Replace with your hex color
        window.setNavigationBarColor(Color.parseColor("#FFFFFF"));

        setDarkNavigationIcons(window);

        // Check if user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Redirect to HomeActivity if already logged in
            startActivity(new Intent(MainActivity.this, dashboard.class));
            finish();
        }

        // Find views by ID
        nameEditText = findViewById(R.id.good_name);
        emailEditText = findViewById(R.id.email_id);
        passwordEditText = findViewById(R.id.pass_word);
        termsCheckBox = findViewById(R.id.tac_checkl);
        registerButton = findViewById(R.id.register_but);
        pb_register = findViewById(R.id.pb_register);
        loginTextView = findViewById(R.id.go_login);

        pb_register.setVisibility(View.GONE);

        // Register button click listener
        registerButton.setOnClickListener(v -> registerUser());

        // Login text click listener
        loginTextView.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, login.class));
            finish();
        });
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!termsCheckBox.isChecked()) {
            Toast.makeText(MainActivity.this, "You must agree to the terms and conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        registerButton.setVisibility(View.INVISIBLE);
        pb_register.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();

                            // Store user data in Firebase Database
                            HashMap<String, String> userData = new HashMap<>();
                            userData.put("userId", userId);
                            userData.put("name", name);
                            userData.put("email", email);

                            databaseReference.child(userId).setValue(userData)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, "User Registered Successfully!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(MainActivity.this, dashboard.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Database Error: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                        registerButton.setVisibility(View.VISIBLE);
                                        pb_register.setVisibility(View.GONE);
                                    });
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        registerButton.setVisibility(View.VISIBLE);
                        pb_register.setVisibility(View.GONE);
                    }
                });
    }

    private void setDarkNavigationIcons(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }
}
