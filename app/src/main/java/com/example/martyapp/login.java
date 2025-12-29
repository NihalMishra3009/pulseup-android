package com.example.martyapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    private EditText email_idl, pass_wordl;
    private Button login_but;
    private CheckBox tac_checkl;
    private TextView go_register;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_marty); // Ensure this matches your XML filename

        // Firebase Database Reference (Ensure "users" is the correct node where emails are stored)
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#0F3E4D")); // Replace with your hex color
        window.setNavigationBarColor(Color.parseColor("#FFFFFF"));

        setDarkNavigationIcons(window);

        // Initialize UI Components
        email_idl = findViewById(R.id.email_idl);
        pass_wordl = findViewById(R.id.pass_wordl);
        login_but = findViewById(R.id.login_but);
        tac_checkl = findViewById(R.id.tac_checkl);
        go_register = findViewById(R.id.go_register);
        progressBar = findViewById(R.id.pb_login);

        // Navigate to Register Page
        go_register.setOnClickListener(view -> {
            startActivity(new Intent(login.this, MainActivity.class));
            finish();
        });

        // Handle Login Click
        login_but.setOnClickListener(view -> {
            String email = email_idl.getText().toString().trim();
            String password = pass_wordl.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(login.this, "Please enter email and password!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!tac_checkl.isChecked()) {
                Toast.makeText(login.this, "You must agree to the terms and conditions!", Toast.LENGTH_SHORT).show();
                return;
            }

            login_but.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            verifyUser(email, password);
        });
    }

    private void verifyUser(String email, String password) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean emailFound = false;

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    // Retrieve the stored email and password from Firebase
                    String storedEmail = userSnapshot.child("email").getValue(String.class);
                    String storedPassword = userSnapshot.child("password").getValue(String.class);

                    Log.d("FirebaseDebug", "Checking email: " + storedEmail);

                    if (storedEmail != null && storedEmail.equals(email)) {
                        emailFound = true;
                        if (storedPassword != null && storedPassword.equals(password)) {
                            Toast.makeText(login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(login.this, dashboard.class));
                            finish();
                        } else {
                            Toast.makeText(login.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
                        }
                        break; // Exit loop once email is found
                    }
                }

                if (!emailFound) {
                    Toast.makeText(login.this, "Entered email is not registered!", Toast.LENGTH_SHORT).show();
                }
                login_but.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(login.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", "Error fetching data: " + error.getMessage());
                login_but.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setDarkNavigationIcons(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }

}
