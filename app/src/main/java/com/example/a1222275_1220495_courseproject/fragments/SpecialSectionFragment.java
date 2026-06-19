package com.example.a1222275_1220495_courseproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1222275_1220495_courseproject.R;
import com.example.a1222275_1220495_courseproject.adapters.SpecialTripAdapter;
import com.example.a1222275_1220495_courseproject.database.DataBaseHelper;
import com.example.a1222275_1220495_courseproject.models.Trip;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class SpecialSectionFragment extends Fragment {

    private RecyclerView rvPopular, rvBestOffers;
    private TextView tvEmptyPopular, tvEmptyOffers;
    private LinearLayout layoutPopular, layoutBestOffers;
    private TabLayout tabLayout;
    private DataBaseHelper dbHelper;
    private long currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_section, container, false);

        // Initialize UI components
        tabLayout = view.findViewById(R.id.tabLayout);
        layoutPopular = view.findViewById(R.id.layoutPopular);
        layoutBestOffers = view.findViewById(R.id.layoutBestOffers);
        
        rvPopular = view.findViewById(R.id.rvPopularDestinations);
        rvBestOffers = view.findViewById(R.id.rvBestOffers);
        tvEmptyPopular = view.findViewById(R.id.tvEmptyPopular);
        tvEmptyOffers = view.findViewById(R.id.tvEmptyOffers);

        dbHelper = new DataBaseHelper(getContext());
        loadUserId();

        // Setup Layout Managers
        rvPopular.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBestOffers.setLayoutManager(new LinearLayoutManager(getContext()));

        setupTabs();
        loadData();

        return view;
    }

    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    layoutPopular.setVisibility(View.VISIBLE);
                    layoutBestOffers.setVisibility(View.GONE);
                } else {
                    layoutPopular.setVisibility(View.GONE);
                    layoutBestOffers.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadUserId() {
        SharedPreferences prefs = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        currentUserId = prefs.getLong("userId", -1);
    }

    private void loadData() {
        // 1. Load Popular Destinations (Rating >= 4.5)
        List<Trip> popularTrips = dbHelper.getPopularDestinations();
        if (popularTrips.isEmpty()) {
            tvEmptyPopular.setVisibility(View.VISIBLE);
            rvPopular.setVisibility(View.GONE);
        } else {
            tvEmptyPopular.setVisibility(View.GONE);
            rvPopular.setVisibility(View.VISIBLE);
            SpecialTripAdapter popularAdapter = new SpecialTripAdapter(getContext(), popularTrips, currentUserId, this::openTripDetails);
            rvPopular.setAdapter(popularAdapter);
        }

        // 2. Load Best Travel Offers (Price Ascending)
        List<Trip> bestOffers = dbHelper.getBestTravelOffers();
        if (bestOffers.isEmpty()) {
            tvEmptyOffers.setVisibility(View.VISIBLE);
            rvBestOffers.setVisibility(View.GONE);
        } else {
            tvEmptyOffers.setVisibility(View.GONE);
            rvBestOffers.setVisibility(View.VISIBLE);
            SpecialTripAdapter offersAdapter = new SpecialTripAdapter(getContext(), bestOffers, currentUserId, this::openTripDetails);
            rvBestOffers.setAdapter(offersAdapter);
        }
    }

    private void openTripDetails(Trip trip) {
        // Navigate to TripDetailsFragment passing the trip ID
        Fragment detailsFragment = TripDetailsFragment.newInstance((int) trip.getTripId());
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailsFragment)
                .addToBackStack(null)
                .commit();
    }
}
