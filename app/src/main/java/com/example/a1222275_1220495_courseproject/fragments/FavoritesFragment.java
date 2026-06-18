package com.example.a1222275_1220495_courseproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.a1222275_1220495_courseproject.adapters.FavoriteAdapter;
import com.example.a1222275_1220495_courseproject.database.DataBaseHelper;
import com.example.a1222275_1220495_courseproject.models.Trip;
import com.example.a1222275_1220495_courseproject.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * FavoritesFragment: Displays a list of trips that the user has marked as favorites.
 * Implements OnFavoriteActionListener to handle remove and reserve actions.
 */
public class FavoritesFragment extends Fragment implements FavoriteAdapter.OnFavoriteActionListener {

    private RecyclerView rvFavorites;
    private FavoriteAdapter adapter;
    private List<Trip> favoriteTrips;
    private DataBaseHelper dbHelper;
    private long currentUserId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        // Initialize UI components and Database Helper
        rvFavorites = view.findViewById(R.id.rvFavorites);
        dbHelper = new DataBaseHelper(getContext());

        // Setup RecyclerView with a Linear Layout Manager
        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load the current user and their favorite trips
        loadFavorites();

        return view;
    }

    /**
     * Retrieves the current user's ID and loads their favorites from the database.
     */
    private void loadFavorites() {
        // Get user email from SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String email = prefs.getString("remember_email", null);

        if (email != null) {
            // Fetch user details to get the ID
            User user = dbHelper.getUserByEmail(email);
            if (user != null) {
                currentUserId = user.getId();
                // Fetch the list of favorite trips from the database
                favoriteTrips = dbHelper.getFavoritesByUser(currentUserId);
            } else {
                favoriteTrips = new ArrayList<>();
            }
        } else {
            favoriteTrips = new ArrayList<>();
        }

        // Initialize and set the adapter
        adapter = new FavoriteAdapter(favoriteTrips, this);
        rvFavorites.setAdapter(adapter);
    }

    /**
     * Callback for the Remove button: Deletes the trip from favorites in the database and updates UI.
     */
    @Override
    public void onRemove(Trip trip, int position) {
        if (currentUserId != -1) {
            // Remove from database
            dbHelper.removeFavorite(currentUserId, trip.getTripId());
            // Remove from local list and notify adapter
            favoriteTrips.remove(position);
            adapter.notifyItemRemoved(position);
            Toast.makeText(getContext(), trip.getDestination() + " removed from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Callback for the Reserve button: Currently shows a placeholder toast.
     */
    @Override
    public void onReserve(Trip trip) {
        // Placeholder for reservation logic
        Toast.makeText(getContext(), "Opening reservation for " + trip.getDestination(), Toast.LENGTH_SHORT).show();
    }
}