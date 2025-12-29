package com.example.martyapp;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class gw_playvid extends AppCompatActivity {

    private VideoView videoView;
    private LinearLayout controlOverlay;
    private ImageButton playPauseBtn, forwardBtn, backwardBtn, rotationBtn;
    private SeekBar seekBar;
    private Spinner speedSpinner;

    private Handler handler = new Handler();
    private Runnable updateSeekBarRunnable;
    private MediaPlayer mediaPlayer;

    private boolean isPortrait = true;
    private final float[] speedValues = {0.5f, 1.0f, 1.5f, 2.0f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        initViews();
        setupVideoPlayer();
        setupControls();
        setupSpeedSpinner();
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#979797")); // Replace with your hex color
        window.setNavigationBarColor(Color.parseColor("#979797"));

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Toolbar toolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);

        // Enable Back Button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Handle Back Button Click


        // Status bar ko wapas laane ka code
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    private void initViews() {
        videoView = findViewById(R.id.videoView);
        controlOverlay = findViewById(R.id.controlOverlay);
        playPauseBtn = findViewById(R.id.playPauseBtn);
        forwardBtn = findViewById(R.id.forwardBtn);
        backwardBtn = findViewById(R.id.backwardBtn);
        rotationBtn = findViewById(R.id.rotationBtn);
        seekBar = findViewById(R.id.seekBar);
        speedSpinner = findViewById(R.id.speedSpinner);
    }

    private void setupVideoPlayer() {
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.fullweek_gym);
        videoView.setVideoURI(videoUri);
        videoView.start();

        videoView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                controlOverlay.setVisibility(controlOverlay.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
            return true;
        });

        videoView.setOnPreparedListener(mp -> {
            mediaPlayer = mp;
            seekBar.setMax(videoView.getDuration());

            updateSeekBarRunnable = new Runnable() {
                @Override
                public void run() {
                    seekBar.setProgress(videoView.getCurrentPosition());
                    handler.postDelayed(this, 500);
                }
            };
            handler.post(updateSeekBarRunnable);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setVideoSpeed(1.0f);
            }
        });

        videoView.setOnCompletionListener(mp -> playPauseBtn.setImageResource(R.drawable.play_button_round_white_icon));
    }

    private void setupControls() {
        playPauseBtn.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.pause();
                playPauseBtn.setImageResource(R.drawable.play_button_round_white_icon);
            } else {
                videoView.start();
                playPauseBtn.setImageResource(R.drawable.pause_button_round_white_icon__1_);
            }
        });

        forwardBtn.setOnClickListener(v -> {
            int newTime = Math.min(videoView.getCurrentPosition() + 5000, videoView.getDuration());
            videoView.seekTo(newTime);
        });

        backwardBtn.setOnClickListener(v -> {
            int newTime = Math.max(videoView.getCurrentPosition() - 5000, 0);
            videoView.seekTo(newTime);
        });

        rotationBtn.setOnClickListener(v -> {
            setRequestedOrientation(isPortrait ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            isPortrait = !isPortrait;
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean wasPlaying = false;

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                wasPlaying = videoView.isPlaying();
                if (wasPlaying) videoView.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoView.seekTo(seekBar.getProgress());
                if (wasPlaying) videoView.start();
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) videoView.seekTo(progress);
            }
        });
    }

    private void setupSpeedSpinner() {
        String[] speedLabels = {"0.5x", "1x", "1.5x", "2x"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, speedLabels) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((android.widget.TextView) view).setTextColor(Color.WHITE);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                ((android.widget.TextView) view).setTextColor(Color.BLACK);
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        speedSpinner.setAdapter(adapter);

        speedSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setVideoSpeed(speedValues[position]);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setVideoSpeed(float speed) {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}