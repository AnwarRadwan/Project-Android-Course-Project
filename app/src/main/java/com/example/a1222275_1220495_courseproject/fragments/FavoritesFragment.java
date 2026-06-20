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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1222275_1220495_courseproject.R;
import com.example.a1222275_1220495_courseproject.adapters.FavoriteAdapter;
import com.example.a1222275_1220495_courseproject.database.DataBaseHelper;
import com.example.a1222275_1220495_courseproject.models.Trip;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Fragment for favorite trips
public class FavoritesFragment extends Fragment implements FavoriteAdapter.OnFavoriteActionListener {

    private RecyclerView rvFavorites;
    private TextView tvEmptyFavorites;
    private FavoriteAdapter adapter;
    private List<Trip> favoriteTrips;
    private DataBaseHelper dbHelper;
    private long currentUserId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        rvFavorites = view.findViewById(R.id.rvFavorites);
        tvEmptyFavorites = view.findViewById(R.id.tvEmptyFavorites);
        dbHelper = new DataBaseHelper(getContext());

        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load data
        loadFavorites();

        return view;
    }

    // Load favorites from db
    private void loadFavorites() {
        SharedPreferences prefs = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        currentUserId = prefs.getLong("userId", -1);

        if (currentUserId != -1) {
            favoriteTrips = dbHelper.getFavoritesByUser(currentUserId);
        } else {
            favoriteTrips = new ArrayList<>();
        }

        updateUI();

        adapter = new FavoriteAdapter(favoriteTrips, this);
        rvFavorites.setAdapter(adapter);
    }

    // Update empty state visibility
    private void updateUI() {
        if (favoriteTrips == null || favoriteTrips.isEmpty()) {
            rvFavorites.setVisibility(View.GONE);
            tvEmptyFavorites.setVisibility(View.VISIBLE);
        } else {
            rvFavorites.setVisibility(View.VISIBLE);
            tvEmptyFavorites.setVisibility(View.GONE);
        }
    }

    // Remove favorite action
    @Override
    public void onRemove(Trip trip, int position) {
        if (currentUserId != -1) {
            dbHelper.removeFavorite(currentUserId, trip.getTripId());
            favoriteTrips.remove(position);
            adapter.notifyItemRemoved(position);
            updateUI();
            Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();
        }
    }

    // Reserve action
    @Override
    public void onReserve(Trip trip) {
        showReservationDialog(trip);
    }

    // Show reservation dialog
    private void showReservationDialog(Trip trip) {
        if (currentUserId == -1) {
            Toast.makeText(getContext(), "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = requireActivity().getLayoutInflater().inflate(R.layout.dialog_reserve, null);
        builder.setView(dialogView);

        EditText etQuantity = dialogView.findViewById(R.id.etQuantity);
        Spinner spinnerType = dialogView.findViewById(R.id.spinnerType);
        MaterialButton btnConfirm = dialogView.findViewById(R.id.btnConfirmReservation);

        String[] types = {"Standard", "VIP", "Premium"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        AlertDialog dialog = builder.create();

        // Handle confirm click
        btnConfirm.setOnClickListener(v -> {
            String qtyStr = etQuantity.getText().toString().trim();
            if (qtyStr.isEmpty()) {
                etQuantity.setError("Enter quantity");
                return;
            }

            try {
                int quantity = Integer.parseInt(qtyStr);
                String type = spinnerType.getSelectedItem().toString();
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                boolean success = dbHelper.insertReservation(currentUserId, trip.getTripId(), quantity, type, date, "Pending");

                if (success) {
                    Toast.makeText(getContext(), "Reserved", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();
    }
}
