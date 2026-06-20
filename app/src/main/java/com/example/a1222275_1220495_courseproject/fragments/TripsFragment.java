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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a1222275_1220495_courseproject.R;
import com.example.a1222275_1220495_courseproject.adapters.TripAdapter;
import com.example.a1222275_1220495_courseproject.database.DataBaseHelper;
import com.example.a1222275_1220495_courseproject.models.Trip;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Fragment for trips list
public class TripsFragment extends Fragment implements TripAdapter.OnTripClickListener {

    private RecyclerView recyclerViewTrips;
    private TripAdapter tripAdapter;
    private DataBaseHelper dbHelper;
    private TextView tvEmptyState;
    private SearchView searchViewTrips;
    private ImageButton btnFilter;
    private long userId;

    private List<Trip> allTripsList = new ArrayList<>();
    private List<Trip> filteredTripsList = new ArrayList<>();

    private String currentQuery = "";
    private String selectedCountry = "All Countries";
    private double minPrice = 0;
    private double maxPrice = Double.MAX_VALUE;
    private double minRating = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        // Init views
        recyclerViewTrips = view.findViewById(R.id.recyclerViewTrips);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        searchViewTrips = view.findViewById(R.id.searchViewTrips);
        btnFilter = view.findViewById(R.id.btnFilterTrips);

        recyclerViewTrips.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DataBaseHelper(getContext());
        
        // Get user id
        SharedPreferences prefs = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        userId = prefs.getLong("userId", -1);

        // Load data
        allTripsList = dbHelper.getAllTrips();
        filteredTripsList.addAll(allTripsList);

        tripAdapter = new TripAdapter(filteredTripsList, this);
        recyclerViewTrips.setAdapter(tripAdapter);
        
        // Setup components
        refreshFavorites();
        setupSearch();
        setupFilterButton();

        return view;
    }

    // Refresh favorites
    private void refreshFavorites() {
        List<Trip> favTrips = dbHelper.getFavoritesByUser(userId);
        List<Long> favIds = new ArrayList<>();
        for (Trip t : favTrips) {
            favIds.add(t.getTripId());
        }
        tripAdapter.setFavoriteTripIds(favIds);
    }

    // Setup search
    private void setupSearch() {
        searchViewTrips.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentQuery = newText;
                applyFilters();
                return true;
            }
        });
    }

    // Setup filter
    private void setupFilterButton() {
        btnFilter.setOnClickListener(v -> showFilterDialog());
    }

    // Show filter dialog
    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filter_trips, null);
        builder.setView(dialogView);

        Spinner spinnerCountry = dialogView.findViewById(R.id.spinnerCountryFilter);
        Spinner spinnerRating = dialogView.findViewById(R.id.spinnerRatingFilter);
        EditText etMin = dialogView.findViewById(R.id.etMinPrice);
        EditText etMax = dialogView.findViewById(R.id.etMaxPrice);
        View btnApply = dialogView.findViewById(R.id.btnApplyFilter);
        View btnReset = dialogView.findViewById(R.id.btnResetFilter);

        // Setup country spinner
        Set<String> countries = new HashSet<>();
        countries.add("All Countries");
        for (Trip t : allTripsList) {
            countries.add(t.getCountry());
        }
        List<String> countryList = new ArrayList<>(countries);
        java.util.Collections.sort(countryList);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, countryList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countryAdapter);

        // Setup rating spinner
        String[] ratings = {"Any Rating", "3.0+", "4.0+", "4.5+"};
        ArrayAdapter<String> ratingAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, ratings);
        ratingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRating.setAdapter(ratingAdapter);

        // Restore filters
        spinnerCountry.setSelection(countryList.indexOf(selectedCountry));
        if (minRating == 0) spinnerRating.setSelection(0);
        else if (minRating == 3.0) spinnerRating.setSelection(1);
        else if (minRating == 4.0) spinnerRating.setSelection(2);
        else if (minRating == 4.5) spinnerRating.setSelection(3);
        
        if (minPrice > 0) etMin.setText(String.valueOf(minPrice));
        if (maxPrice < Double.MAX_VALUE) etMax.setText(String.valueOf(maxPrice));

        AlertDialog dialog = builder.create();

        // Apply click
        btnApply.setOnClickListener(view -> {
            selectedCountry = spinnerCountry.getSelectedItem().toString();
            
            String ratingStr = spinnerRating.getSelectedItem().toString();
            if (ratingStr.equals("Any Rating")) minRating = 0;
            else minRating = Double.parseDouble(ratingStr.replace("+", ""));

            String minStr = etMin.getText().toString();
            minPrice = minStr.isEmpty() ? 0 : Double.parseDouble(minStr);

            String maxStr = etMax.getText().toString();
            maxPrice = maxStr.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxStr);

            applyFilters();
            dialog.dismiss();
        });

        // Reset click
        btnReset.setOnClickListener(view -> {
            selectedCountry = "All Countries";
            minPrice = 0;
            maxPrice = Double.MAX_VALUE;
            minRating = 0;
            applyFilters();
            dialog.dismiss();
        });

        dialog.show();
    }

    // Apply filtering logic
    private void applyFilters() {
        filteredTripsList.clear();
        String query = currentQuery.toLowerCase().trim();

        for (Trip trip : allTripsList) {
            boolean matchesSearch = trip.getDestination().toLowerCase().contains(query) || 
                                   trip.getCountry().toLowerCase().contains(query);
            
            boolean matchesCountry = selectedCountry.equals("All Countries") || 
                                    trip.getCountry().equals(selectedCountry);
            
            boolean matchesPrice = trip.getPrice() >= minPrice && trip.getPrice() <= maxPrice;
            
            boolean matchesRating = trip.getRating() >= minRating;

            if (matchesSearch && matchesCountry && matchesPrice && matchesRating) {
                filteredTripsList.add(trip);
            }
        }

        tripAdapter.updateList(filteredTripsList);

        // Handle empty state
        if (filteredTripsList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            recyclerViewTrips.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            recyclerViewTrips.setVisibility(View.VISIBLE);
        }
    }

    // Handle trip click
    @Override
    public void onTripClick(Trip trip) {
        TripDetailsFragment detailsFragment = TripDetailsFragment.newInstance((int) trip.getTripId());
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, detailsFragment)
                .addToBackStack(null)
                .commit();
    }

    // Handle favorite click
    @Override
    public void onFavoriteClick(Trip trip) {
        if (userId == -1) {
            Toast.makeText(getContext(), "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.isFavorite((int) userId, (int) trip.getTripId())) {
            dbHelper.removeFavorite(userId, trip.getTripId());
            Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.addFavorite((int) userId, (int) trip.getTripId());
            Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
        }
        refreshFavorites();
    }
}
