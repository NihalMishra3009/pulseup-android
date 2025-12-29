package com.example.martyapp;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.RenderMode;

public class lotsplash extends Activity {

    private LottieAnimationView bottomAnimation;
    private View splashScreenLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashnew);

        // **Fix: Keep background white to avoid black screen issue**
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        // **Set Status & Navigation Bar Colors**
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#FFFFFF"));
        window.setNavigationBarColor(Color.parseColor("#FFFFFF"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        // **Initialize animation views**
        splashScreenLayout = findViewById(R.id.splashnew);
        bottomAnimation = findViewById(R.id.bottomAnimation);

        // **Lottie animation settings**
        bottomAnimation.setAnimation("smallanimation.json");
        bottomAnimation.setSpeed(1.5f);
        bottomAnimation.setRepeatCount(0);
        bottomAnimation.setRenderMode(RenderMode.HARDWARE);

        // **Start animation**
        bottomAnimation.playAnimation();

        // **When Lottie animation finishes, trigger slide-left animation**
        bottomAnimation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                startSlideLeftAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
    }

    private void startSlideLeftAnimation() {
        // **Apply slide-left animation**
        Animation slideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left);
        splashScreenLayout.startAnimation(slideLeft);

        slideLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                // **Fix: Remove splash instantly & launch MainActivity**
                Intent intent = new Intent(lotsplash.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(0, 0); // **No unwanted transition effect**
                finishAffinity();  // **Remove splash COMPLETELY**
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }
}
