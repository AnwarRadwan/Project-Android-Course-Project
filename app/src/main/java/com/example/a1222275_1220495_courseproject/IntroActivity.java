package com.example.a1222275_1220495_courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a1222275_1220495_courseproject.database.DataBaseHelper;
import com.example.a1222275_1220495_courseproject.models.Trip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IntroActivity extends AppCompatActivity {

    private static final String TAG = "IntroActivity";
    private TextView introTitle, introDescription;
    private Button btnConnect;
    private ProgressBar progressBar;
    private DataBaseHelper dbHelper;
    // Mock API URL
    private static final String API_URL = "https://mocki.io/v1/ee195475-f2ec-4ab3-aecc-828ef0c92985";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Initialize UI components
        introTitle = findViewById(R.id.introTitle);
        introDescription = findViewById(R.id.introDescription);
        btnConnect = findViewById(R.id.btnConnect);
        progressBar = findViewById(R.id.progressBar);
        dbHelper = new DataBaseHelper(this);

        // Load and start slide_up animation for title and description
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        introTitle.startAnimation(slideUp);
        introDescription.startAnimation(slideUp);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchTripsData();
            }
        });
    }

    private void fetchTripsData() {
        // Show progress bar and disable button
        progressBar.setVisibility(View.VISIBLE);
        btnConnect.setEnabled(false);

        // Use ExecutorService for background network task
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            boolean success = false;
            String errorMessage = "Connection Failed. Please try again.";
            try {
                // Networking logic
                Log.d(TAG, "Connecting to: " + API_URL);
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000); // Increased timeout
                conn.setReadTimeout(10000);

                int responseCode = conn.getResponseCode();
                Log.d(TAG, "Response Code: " + responseCode);

                if (responseCode == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();

                    Log.d(TAG, "JSON Received: " + response.toString());

                    // Parse JSON and save to SQLite
                    JSONArray tripsArray = new JSONArray(response.toString());
                    dbHelper.deleteAllTrips(); // Clear old data

                    for (int i = 0; i < tripsArray.length(); i++) {
                        JSONObject obj = tripsArray.getJSONObject(i);
                        Trip trip = new Trip();
                        
                        // Using opt methods to handle missing fields gracefully
                        trip.setTripId(obj.optLong("id"));
                        trip.setDestination(obj.optString("destination"));
                        trip.setCountry(obj.optString("country"));
                        
                        // Try duration_days or duration
                        int duration = obj.optInt("duration_days", obj.optInt("duration", 0));
                        trip.setDuration(duration);
                        
                        trip.setPrice(obj.optDouble("price", 0.0));
                        trip.setRating(obj.optDouble("rating", 0.0));
                        trip.setDescription(obj.optString("description"));
                        trip.setImage(obj.optString("image"));

                        dbHelper.insertTrip(trip);
                    }
                    success = true;
                } else {
                    errorMessage = "Server error code: " + responseCode;
                }
            } catch (Exception e) {
                Log.e(TAG, "Network Error: ", e);
                errorMessage = "Network Error: " + e.getLocalizedMessage();
            }

            final boolean finalSuccess = success;
            final String finalError = errorMessage;
            handler.post(() -> {
                progressBar.setVisibility(View.GONE);
                btnConnect.setEnabled(true);

                if (finalSuccess) {
                    Toast.makeText(IntroActivity.this, "Connection Successful!", Toast.LENGTH_SHORT).show();
                    // Navigate to LoginActivity
                    Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e(TAG, finalError);
                    Toast.makeText(IntroActivity.this, finalError, Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
