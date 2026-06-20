package com.example.a1222275_1220495_courseproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1222275_1220495_courseproject.AddEditTripActivity;
import com.example.a1222275_1220495_courseproject.R;
import com.example.a1222275_1220495_courseproject.adapters.AdminTripAdapter;
import com.example.a1222275_1220495_courseproject.database.DataBaseHelper;
import com.example.a1222275_1220495_courseproject.models.Trip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

// Fragment for admin to manage trips
public class ManageTripsFragment extends Fragment implements AdminTripAdapter.OnTripActionListener {

    private RecyclerView rvTrips;
    private AdminTripAdapter adapter;
    private DataBaseHelper dbHelper;
    private FloatingActionButton fabAddTrip;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_manage_trips, container, false);

        dbHelper = new DataBaseHelper(requireContext());
        rvTrips = view.findViewById(R.id.rvAdminTrips);
        fabAddTrip = view.findViewById(R.id.fabAddTrip);

        rvTrips.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Add trip button
        fabAddTrip.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddEditTripActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTrips();
    }

    // Load trips from db
    private void loadTrips() {
        List<Trip> trips = dbHelper.getAllTrips();
        if (adapter == null) {
            adapter = new AdminTripAdapter(trips, this);
            rvTrips.setAdapter(adapter);
        } else {
            adapter.updateList(trips);
        }
    }

    // Edit trip action
    @Override
    public void onEdit(Trip trip) {
        Intent intent = new Intent(getActivity(), AddEditTripActivity.class);
        intent.putExtra("TRIP_ID", trip.getTripId());
        startActivity(intent);
    }

    // Delete trip action
    @Override
    public void onDelete(Trip trip) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Trip")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteTrip(trip.getTripId());
                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    loadTrips();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
