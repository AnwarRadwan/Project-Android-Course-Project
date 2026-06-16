package com.example.a1222275_1220495_courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ImageView imageViewLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageViewLogo = findViewById(R.id.imageViewLogo);

        Animation animation =
                AnimationUtils.loadAnimation(
                        SplashActivity.this,
                        R.anim.fade_in);

        imageViewLogo.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent =
                        new Intent(
                                SplashActivity.this,
                                IntroductionActivity.class);

                startActivity(intent);

                finish();
            }
        },3000);
    }
}