package com.example.martyapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class homepage extends AppCompatActivity {

    private Button logoutButton;
    private TextView welcomeText;
    private ProgressBar pbUsername, pbLogout;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_marty);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#0F3E4D")); // Replace with your hex color
        window.setNavigationBarColor(Color.parseColor("#FFFFFF"));

        setDarkNavigationIcons(window);

        Toolbar toolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);

        // Enable Back Button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Handle Back Button Click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Go back to previous activity
            }
        });

        // Initialize FirebaseAuth and DatabaseReference
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Check if user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(homepage.this, MainActivity.class));
            finish();
        }

        // Find views
        welcomeText = findViewById(R.id.welcom_text);
        logoutButton = findViewById(R.id.logout_button);
        pbUsername = findViewById(R.id.pb_username);
        pbLogout = findViewById(R.id.pb_logout);

        // Show username loading progress bar
        pbUsername.setVisibility(View.VISIBLE);

        // Fetch and display user's first name
        if (currentUser != null) {
            String userId = currentUser.getUid();
            usersRef.child(userId).child("name").get().addOnCompleteListener(task -> {
                pbUsername.setVisibility(View.GONE); // Hide progress bar once data is fetched
                if (task.isSuccessful() && task.getResult().getValue() != null) {
                    String userName = task.getResult().getValue().toString();
                    welcomeText.setText("" + userName + "");
                } else {
                    welcomeText.setText("Welcome!");
                }
            });
        }

        // Logout button click listener
        logoutButton.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            pbLogout.setVisibility(View.VISIBLE); // Show logout progress bar
            mAuth.signOut(); // Logout user
            Intent intent = new Intent(homepage.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss(); // Close dialog
        });

        AlertDialog dialog = builder.create();
        dialog.show();



        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setDarkNavigationIcons(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }

}
