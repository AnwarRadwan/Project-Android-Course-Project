package com.example.a1222275_1220495_courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY_MS = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Run animations on the centered container
        View splashContainer = findViewById(R.id.splash_container);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(fadeIn);
        animationSet.addAnimation(scaleUp);
        animationSet.setFillAfter(true);

        splashContainer.startAnimation(animationSet);

        // Navigate to IntroductionActivity after the splash delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, IntroductionActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY_MS);
    }
}
