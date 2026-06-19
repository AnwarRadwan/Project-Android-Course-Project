package com.example.a1222275_1220495_courseproject.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.a1222275_1220495_courseproject.R;
import com.example.a1222275_1220495_courseproject.database.DataBaseHelper;
import com.example.a1222275_1220495_courseproject.models.Trip;
import com.example.a1222275_1220495_courseproject.models.User;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TripDetailsFragment extends Fragment {

    private static final String ARG_TRIP_ID = "trip_id";
    private int tripId;
    private Trip trip;
    private DataBaseHelper dbHelper;
    private long currentUserId = -1;

    private ImageView imgTripDetail;
    private TextView tvDestination, tvCountry, tvDuration, tvPrice, tvRating, tvDescription;
    private ImageButton btnFavorite;
    private MaterialButton btnReserve;

    private boolean isFavorite = false;

    public static TripDetailsFragment newInstance(int tripId) {
        TripDetailsFragment fragment = new TripDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TRIP_ID, tripId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tripId = getArguments().getInt(ARG_TRIP_ID);
        }
        dbHelper = new DataBaseHelper(getContext());
        loadCurrentUser();
    }

    private void loadCurrentUser() {
        SharedPreferences prefs = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        currentUserId = prefs.getLong("userId", -1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_details, container, false);

        imgTripDetail = view.findViewById(R.id.imgTripDetail);
        tvDestination = view.findViewById(R.id.tvDestinationDetail);
        tvCountry = view.findViewById(R.id.tvCountryDetail);
        tvDuration = view.findViewById(R.id.tvDurationDetail);
        tvPrice = view.findViewById(R.id.tvPriceDetail);
        tvRating = view.findViewById(R.id.tvRatingDetail);
        tvDescription = view.findViewById(R.id.tvDescriptionDetail);
        btnFavorite = view.findViewById(R.id.btnFavorite);
        btnReserve = view.findViewById(R.id.btnReserve);

        trip = dbHelper.getTripById(tripId);
        if (trip != null) {
            bindData();
        }

        setupListeners();

        return view;
    }

    private void bindData() {
        tvDestination.setText("Destination: " + trip.getDestination());
        tvCountry.setText("Country: " + trip.getCountry());
        tvDuration.setText("Duration: " + trip.getDuration() + " Days");
        tvPrice.setText("Price: $" + trip.getPrice());
        tvRating.setText("Rating: " + trip.getRating());
        tvDescription.setText(trip.getDescription());

        Glide.with(this)
                .load(trip.getImage())
                .placeholder(R.color.light_gray)
                .into(imgTripDetail);

        if (currentUserId != -1) {
            isFavorite = dbHelper.isFavorite((int) currentUserId, tripId);
            updateFavoriteIcon();
        }
    }

    private void setupListeners() {
        btnFavorite.setOnClickListener(v -> {
            if (currentUserId == -1) {
                Toast.makeText(getContext(), "Please login to favorite trips", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isFavorite) {
                dbHelper.removeFavorite((int) currentUserId, tripId);
                isFavorite = false;
                Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.addFavorite((int) currentUserId, tripId);
                isFavorite = true;
                Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
            }
            updateFavoriteIcon();
        });

        btnReserve.setOnClickListener(v -> showReservationDialog());
    }

    private void updateFavoriteIcon() {
        if (isFavorite) {
            btnFavorite.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            btnFavorite.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }

    private void showReservationDialog() {
        if (currentUserId == -1) {
            Toast.makeText(getContext(), "Please login to make a reservation", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_reserve, null);
        builder.setView(dialogView);

        EditText etQuantity = dialogView.findViewById(R.id.etQuantity);
        Spinner spinnerType = dialogView.findViewById(R.id.spinnerType);
        MaterialButton btnConfirm = dialogView.findViewById(R.id.btnConfirmReservation);

        String[] types = {"Standard", "VIP"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        AlertDialog dialog = builder.create();

        btnConfirm.setOnClickListener(v -> {
            String qtyStr = etQuantity.getText().toString();
            if (qtyStr.isEmpty()) {
                etQuantity.setError("Enter quantity");
                return;
            }

            int quantity = Integer.parseInt(qtyStr);
            String type = spinnerType.getSelectedItem().toString();
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            dbHelper.insertReservation(currentUserId, tripId, quantity, type, date, "Pending");

            Toast.makeText(getContext(), "Reservation successful!", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}