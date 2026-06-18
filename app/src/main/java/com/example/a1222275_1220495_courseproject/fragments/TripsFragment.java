package com.example.a1222275_1220495_courseproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a1222275_1220495_courseproject.R;
import com.example.a1222275_1220495_courseproject.adapters.TripAdapter;
import com.example.a1222275_1220495_courseproject.database.DataBaseHelper;
import com.example.a1222275_1220495_courseproject.models.Trip;
import java.util.List;

public class TripsFragment extends Fragment implements TripAdapter.OnTripClickListener {

    private RecyclerView recyclerViewTrips;
    private TripAdapter tripAdapter;
    private DataBaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        recyclerViewTrips = view.findViewById(R.id.recyclerViewTrips);
        recyclerViewTrips.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DataBaseHelper(getContext());
        List<Trip> tripList = dbHelper.getAllTrips();

        tripAdapter = new TripAdapter(tripList, this);
        recyclerViewTrips.setAdapter(tripAdapter);

        return view;
    }

    @Override
    public void onTripClick(Trip trip) {
        TripDetailsFragment detailsFragment = TripDetailsFragment.newInstance((int) trip.getTripId());

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, detailsFragment)
                .addToBackStack(null)
                .commit();
    }
}
