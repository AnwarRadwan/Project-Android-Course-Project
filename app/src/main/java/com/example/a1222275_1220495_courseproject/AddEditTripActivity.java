package com.example.a1222275_1220495_courseproject;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a1222275_1220495_courseproject.database.DataBaseHelper;
import com.example.a1222275_1220495_courseproject.models.Trip;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddEditTripActivity extends AppCompatActivity {

    private TextInputEditText etDestination, etCountry, etDuration, etPrice, etRating, etDescription, etImageUrl;
    private MaterialButton btnSaveTrip;
    private TextView tvTitle;
    private DataBaseHelper dbHelper;
    private long tripId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_trip);

        dbHelper = new DataBaseHelper(this);

        initViews();

        if (getIntent().hasExtra("TRIP_ID")) {
            tripId = getIntent().getLongExtra("TRIP_ID", -1);
            isEditMode = true;
            tvTitle.setText("Edit Trip Details");
            loadTripData();
        }

        btnSaveTrip.setOnClickListener(v -> saveTrip());
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvAddEditTripTitle);
        etDestination = findViewById(R.id.etTripDestination);
        etCountry = findViewById(R.id.etTripCountry);
        etDuration = findViewById(R.id.etTripDuration);
        etPrice = findViewById(R.id.etTripPrice);
        etRating = findViewById(R.id.etTripRating);
        etDescription = findViewById(R.id.etTripDescription);
        etImageUrl = findViewById(R.id.etTripImageUrl);
        btnSaveTrip = findViewById(R.id.btnSaveTrip);
    }

    private void loadTripData() {
        Trip trip = dbHelper.getTripById((int) tripId);
        if (trip != null) {
            etDestination.setText(trip.getDestination());
            etCountry.setText(trip.getCountry());
            etDuration.setText(String.valueOf(trip.getDuration()));
            etPrice.setText(String.valueOf(trip.getPrice()));
            etRating.setText(String.valueOf(trip.getRating()));
            etDescription.setText(trip.getDescription());
            etImageUrl.setText(trip.getImage());
        }
    }

    private void saveTrip() {
        String dest = etDestination.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        String durationStr = etDuration.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String ratingStr = etRating.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String img = etImageUrl.getText().toString().trim();

        if (dest.isEmpty() || country.isEmpty() || durationStr.isEmpty() || priceStr.isEmpty() || ratingStr.isEmpty() || desc.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int duration = Integer.parseInt(durationStr);
        double price = Double.parseDouble(priceStr);
        double rating = Double.parseDouble(ratingStr);

        if (rating < 1.0 || rating > 5.0) {
            etRating.setError("Rating must be between 1.0 and 5.0");
            return;
        }

        Trip trip = new Trip(tripId, dest, country, duration, price, rating, desc, img);

        if (isEditMode) {
            dbHelper.updateTrip(trip);
            Toast.makeText(this, "Trip updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.insertTrip(trip);
            Toast.makeText(this, "Trip added successfully", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
