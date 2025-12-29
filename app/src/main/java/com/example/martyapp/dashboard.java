package com.example.martyapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Build;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Surface;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.TextureView;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class dashboard extends AppCompatActivity {

    private TextView userrname, usernamedrawer, setschedue, userEmailDrawer, writenow;
    private DrawerLayout drawerLayout;
    private ImageButton pulse_chat, BMI_calcy, step_counting, home_work, gym_work, yoga_asanas, dietkaro, linkedins, instagrams;
    private TextureView textureView;
    private MediaPlayer mediaPlayer;

    private ImageView notify1;
    private ProgressBar pb_welcomsg, pb_demovideo;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_marty);


        // Set status and navigation bar color
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#0F3E4D"));
        window.setNavigationBarColor(Color.parseColor("#FFFFFF"));

        // Set dark navigation bar icons
        setDarkNavigationIcons(window);

        // Initialize UI elements
        setschedue = findViewById(R.id.setschedue);
        pulse_chat = findViewById(R.id.pulse_chat);
        BMI_calcy = findViewById(R.id.BMI_calcy);
        step_counting = findViewById(R.id.step_counting);
        home_work = findViewById(R.id.home_work);
        gym_work = findViewById(R.id.gym_work);
        yoga_asanas = findViewById(R.id.yoga_asanas);
        dietkaro = findViewById(R.id.dietkaro);
        linkedins = findViewById(R.id.linkedins);
        instagrams = findViewById(R.id.instagrams);
        writenow = findViewById(R.id.writenow);
        pb_welcomsg = findViewById(R.id.pb_welcomsg);
        pb_demovideo = findViewById(R.id.pb_demovideo);
        userrname = findViewById(R.id.userrname);
        linkedins = findViewById(R.id.linkedins);
        instagrams = findViewById(R.id.instagrams);
        notify1 = findViewById(R.id.notify1);

        linkedins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(dashboard.this, "Opening LinkedIn profile", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/nirbhaya-suraksha-92b19531b?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app"));
                intent.setPackage("com.linkedin.android");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    // LinkedIn app is not installed, open in browser
                    intent.setPackage(null);
                    startActivity(intent);
                }
            }
        });

        instagrams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(dashboard.this, "Opening Instagram profile", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("https://www.instagram.com/pulseupofficial?igsh=MW1vZG8za2FzdDNseQ==");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.instagram.android");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    // Instagram app is not installed, open in browser
                    intent.setPackage(null);
                    startActivity(intent);
                }
            }
        });


        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        usernamedrawer = headerView.findViewById(R.id.usernamedrawer);
        userEmailDrawer = headerView.findViewById(R.id.user_email_drawer);

        pb_welcomsg.setVisibility(View.VISIBLE);
        pb_demovideo.setVisibility(View.VISIBLE);

        // Setup drawer and toolbar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.getOverflowIcon().setTint(Color.WHITE);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            handleNavigationClick(item);
            return true;
        });

        // Button Click Listeners
        setschedue.setOnClickListener(view -> startActivity(new Intent(dashboard.this, schedules.class)));
        pulse_chat.setOnClickListener(view -> startActivity(new Intent(dashboard.this, nayachabot_varvanta.class)));
        BMI_calcy.setOnClickListener(view -> startActivity(new Intent(dashboard.this, bmimain.class)));
        step_counting.setOnClickListener(view -> startActivity(new Intent(dashboard.this, stepcounting.class)));
        home_work.setOnClickListener(view -> startActivity(new Intent(dashboard.this, homeworkout_j.class)));
        gym_work.setOnClickListener(view -> startActivity(new Intent(dashboard.this, gymworkout_j.class)));
        yoga_asanas.setOnClickListener(view -> startActivity(new Intent(dashboard.this, yoga_wo_j.class)));
        dietkaro.setOnClickListener(view -> startActivity(new Intent(dashboard.this, diet_pu_j.class)));
        writenow.setOnClickListener(view -> startActivity(new Intent(dashboard.this, feedbackj.class)));
        notify1.setOnClickListener(view -> startActivity(new Intent(dashboard.this, notifi.class)));


        // Firebase Initialization
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Fetch Username and Email from Firebase
        fetchUserDetails();

        // Video Player Setup
        textureView = findViewById(R.id.textureView);

        textureView.setOnClickListener(view -> startActivity(new Intent(dashboard.this, medit_j.class)));

        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                playVideo(new Surface(surface));
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {}
            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) { return false; }
            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {}
        });

        textureView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (textureView.isShown()) {
                pb_demovideo.setVisibility(View.GONE);
            }
        });
    }

    private void setDarkNavigationIcons(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = window.getInsetsController();
            if (controller != null) {
                controller.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                );
            }
        } else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }

    private void fetchUserDetails() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(dashboard.this, login.class));
            finish();
            return;
        }

        String userId = currentUser.getUid();
        String userEmail = currentUser.getEmail();

        usersRef.child(userId).child("name").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().getValue() != null) {
                String userName = task.getResult().getValue().toString();
                userrname.setText("Hey " + userName + " checkout new activity !");
                usernamedrawer.setText(userName);
            } else {
                userrname.setText("Welcome!");
                usernamedrawer.setText("Welcome!");
            }
            pb_welcomsg.setVisibility(View.GONE);
        });

        // Set email in navigation drawer
        if (userEmailDrawer != null && userEmail != null) {
            userEmailDrawer.setText(userEmail);
        }
    }

    private void playVideo(Surface surface) {
        mediaPlayer = MediaPlayer.create(this, R.raw.pulseuppro);
        if (mediaPlayer != null) {
            mediaPlayer.setSurface(surface);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void handleNavigationClick(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            startActivity(new Intent(dashboard.this, dashboard.class));
        } else if (itemId == R.id.nav_profile) {
            startActivity(new Intent(dashboard.this, homepage.class));
        } else if (itemId == R.id.nav_settings) {
            startActivity(new Intent(dashboard.this, SettingsActivity.class));
        } else if (itemId == R.id.nav_logout) {
            startActivity(new Intent(dashboard.this, homepage.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}
