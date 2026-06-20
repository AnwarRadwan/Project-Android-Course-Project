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

// Splash screen activity
public class SplashActivity extends AppCompatActivity {

    private ImageView imageViewLogo;
    private TextView textViewAppName;
    private TextView textViewTagline;
    private View mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Init views
        mainLayout = findViewById(R.id.mainSplashLayout);
        imageViewLogo = findViewById(R.id.imageViewLogo);
        textViewAppName = findViewById(R.id.textViewAppName);
        textViewTagline = findViewById(R.id.textViewTagline);

        // Load animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);

        // Setup logo animation
        AnimationSet logoAnimationSet = new AnimationSet(true);
        logoAnimationSet.addAnimation(zoomIn);
        logoAnimationSet.addAnimation(rotate);

        // Start animations
        mainLayout.startAnimation(fadeIn);
        imageViewLogo.startAnimation(logoAnimationSet);
        textViewAppName.startAnimation(slideUp);
        textViewTagline.startAnimation(slideUp);

        // Delayed navigation
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
