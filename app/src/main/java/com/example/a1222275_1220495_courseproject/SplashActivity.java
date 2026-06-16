package com.example.a1222275_1220495_courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ImageView imageViewLogo;
    private TextView textViewAppName;
    private TextView textViewTagline;
    private View mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize UI components
        mainLayout = findViewById(R.id.mainSplashLayout);
        imageViewLogo = findViewById(R.id.imageViewLogo);
        textViewAppName = findViewById(R.id.textViewAppName);
        textViewTagline = findViewById(R.id.textViewTagline);

        // Load all 4 animation files
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);

        // Create an AnimationSet for the Logo to combine Zoom and Rotate
        AnimationSet logoAnimationSet = new AnimationSet(true);
        logoAnimationSet.addAnimation(zoomIn);
        logoAnimationSet.addAnimation(rotate);

        // Start animations based on requirements
        // 1. Fade in for the overall background/layout
        mainLayout.startAnimation(fadeIn);

        // 2. Rotate + Zoom In for the logo
        imageViewLogo.startAnimation(logoAnimationSet);

        // 3. Slide up for the app name and tagline
        textViewAppName.startAnimation(slideUp);
        textViewTagline.startAnimation(slideUp);

        // Navigation logic after 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
