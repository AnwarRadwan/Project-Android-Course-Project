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

// Fragment for user reservations
public class MyReservationsFragment extends Fragment {

    private RecyclerView rvReservations;
    private LinearLayout layoutEmptyState;
    private DataBaseHelper dbHelper;
    private ReservationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_my_reservations, container, false);

        // Init views
        rvReservations = view.findViewById(R.id.rvReservations);
        layoutEmptyState = view.findViewById(R.id.layoutEmptyState);

        rvReservations.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DataBaseHelper(getContext());

        loadReservations();

        return view;
    }

    // Load reservations
    private void loadReservations() {
        try {
            // Get user id
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
            long userId = sharedPreferences.getLong("userId", -1);

            if (userId != -1) {
                List<Reservation> reservationList = dbHelper.getUserReservations(userId);

                if (reservationList != null && !reservationList.isEmpty()) {
                    rvReservations.setVisibility(View.VISIBLE);
                    layoutEmptyState.setVisibility(View.GONE);

                    adapter = new ReservationAdapter(reservationList);
                    rvReservations.setAdapter(adapter);
                } else {
                    showEmptyState();
                }
            } else {
                Toast.makeText(getContext(), "Please login", Toast.LENGTH_SHORT).show();
                showEmptyState();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            showEmptyState();
        }
    }

    // Handle empty state
    private void showEmptyState() {
        rvReservations.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.VISIBLE);
    }
}
