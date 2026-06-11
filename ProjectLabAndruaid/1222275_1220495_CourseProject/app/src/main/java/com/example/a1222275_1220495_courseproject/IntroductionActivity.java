package com.example.a1222275_1220495_courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroductionActivity extends AppCompatActivity {

    private Button btnConnect;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        btnConnect = findViewById(R.id.btn_connect);
        progressBar = findViewById(R.id.progress_bar);

        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        findViewById(android.R.id.content).startAnimation(slideUp);

        btnConnect.setOnClickListener(v -> connectToApi());
    }

    private void connectToApi() {
        btnConnect.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getInstance()
                .getTripApiService()
                .getTrips()
                .enqueue(new Callback<List<Trip>>() {
                    @Override
                    public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                        progressBar.setVisibility(View.GONE);
                        btnConnect.setEnabled(true);

                        if (response.isSuccessful() && response.body() != null) {
                            List<Trip> trips = response.body();
                            DatabaseHelper.getInstance(IntroductionActivity.this)
                                    .insertOrReplaceTrips(trips);
                            Toast.makeText(IntroductionActivity.this,
                                    trips.size() + " trips loaded successfully!",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(IntroductionActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(IntroductionActivity.this,
                                    "Connection failed. Please try again.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Trip>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        btnConnect.setEnabled(true);
                        Toast.makeText(IntroductionActivity.this,
                                "Network error: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
