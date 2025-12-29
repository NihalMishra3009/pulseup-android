package com.example.martyapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class pre_dumblelateralraises_j extends AppCompatActivity {

    private TextureView textureView;

    private TextView gotosteps;
    private MediaPlayer mediaPlayer;

    private ProgressBar pb_1;
    // Steps Data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_dumblelateralraises);
        pb_1 = findViewById(R.id.pb_1);

        textureView = findViewById(R.id.textureView);
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
                pb_1.setVisibility(View.GONE);
            }
        });

        gotosteps = findViewById(R.id.gotosteps);

        gotosteps.setOnClickListener(view -> {
            Toast.makeText(pre_dumblelateralraises_j.this, "Exercise started!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(pre_dumblelateralraises_j.this, dumblelateralraises_j.class));
        });

        // Toolbar Setup
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#0F3E4D"));
        window.setNavigationBarColor(Color.parseColor("#FFFFFF"));

        // Set dark navigation bar icons
        setDarkNavigationIcons(window);

        Toolbar toolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);

        // Enable Back Button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Handle Back Button Click
        toolbar.setNavigationOnClickListener(v -> onBackPressed());


    }

    private void playVideo(Surface surface) {
        mediaPlayer = MediaPlayer.create(this, R.raw.dumblelateralraises_gw);
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

    private void setDarkNavigationIcons(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }

}
