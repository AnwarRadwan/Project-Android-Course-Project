package com.example.a1222275_1220495_courseproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1222275_1220495_courseproject.R;
import com.example.a1222275_1220495_courseproject.adapters.ReservationAdapter;
import com.example.a1222275_1220495_courseproject.database.DataBaseHelper;
import com.example.a1222275_1220495_courseproject.models.Reservation;

import java.util.List;

/**
 * Fragment to display the user's reservations.
 * Fetches data from SQLite and handles empty state UI.
 */
public class MyReservationsFragment extends Fragment {

    private RecyclerView rvReservations;
    private LinearLayout layoutEmptyState;
    private DataBaseHelper dbHelper;
    private ReservationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_reservations, container, false);

        // Initialize UI components
        rvReservations = view.findViewById(R.id.rvReservations);
        layoutEmptyState = view.findViewById(R.id.layoutEmptyState);

        // Setup RecyclerView
        rvReservations.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DataBaseHelper(getContext());

        loadReservations();

        return view;
    }

    /**
     * Loads reservations for the logged-in user from the database.
     */
    private void loadReservations() {
        try {
            // Retrieve userId from SharedPreferences (Assumed saved during login)
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
            long userId = sharedPreferences.getLong("userId", -1);

            if (userId != -1) {
                // Using the updated method name from DataBaseHelper
                List<Reservation> reservationList = dbHelper.getUserReservations(userId);

                if (reservationList != null && !reservationList.isEmpty()) {
                    // Show RecyclerView and hide empty state
                    rvReservations.setVisibility(View.VISIBLE);
                    layoutEmptyState.setVisibility(View.GONE);

                    adapter = new ReservationAdapter(reservationList);
                    rvReservations.setAdapter(adapter);
                } else {
                    // No reservations found
                    showEmptyState();
                }
            } else {
                // User not logged in or ID not found
                Toast.makeText(getContext(), "Please login to view your reservations", Toast.LENGTH_SHORT).show();
                showEmptyState();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error loading reservations: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            showEmptyState();
        }
    }

    private void showEmptyState() {
        rvReservations.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.VISIBLE);
    }
}