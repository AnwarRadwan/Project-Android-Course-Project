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
import com.example.a1222275_1220495_courseproject.adapters.TripAdapter;
import com.example.a1222275_1220495_courseproject.database.DataBaseHelper;
import com.example.a1222275_1220495_courseproject.models.Trip;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements TripAdapter.OnTripClickListener {

    private RecyclerView rvTrips;
    private TripAdapter adapter;
    private DataBaseHelper dbHelper;
    private long userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvTrips = view.findViewById(R.id.rvTrips);
        rvTrips.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DataBaseHelper(getContext());
        
        // جلب ID المستخدم المسجل حالياً
        SharedPreferences prefs = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        userId = prefs.getLong("userId", -1);

        loadTrips();

        return view;
    }

    private void loadTrips() {
        List<Trip> trips = dbHelper.getAllTrips();
        adapter = new TripAdapter(trips, this);
        
        // جلب قائمة الرحلات المفضلة للمستخدم الحالي لتلوين القلوب
        refreshFavorites();
        
        rvTrips.setAdapter(adapter);
    }

    private void refreshFavorites() {
        List<Trip> favTrips = dbHelper.getFavoritesByUser(userId);
        List<Long> favIds = new ArrayList<>();
        for (Trip t : favTrips) {
            favIds.add(t.getTripId());
        }
        adapter.setFavoriteTripIds(favIds);
    }

    @Override
    public void onTripClick(Trip trip) {
        // الانتقال لصفحة التفاصيل عند الضغط على الرحلة
        TripDetailsFragment detailsFragment = TripDetailsFragment.newInstance((int) trip.getTripId());
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, detailsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFavoriteClick(Trip trip) {
        if (userId == -1) {
            Toast.makeText(getContext(), "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.isFavorite((int) userId, (int) trip.getTripId())) {
            dbHelper.removeFavorite(userId, trip.getTripId());
            Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.addFavorite((int) userId, (int) trip.getTripId());
            Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
        }
        // تحديث القلوب في الصفحة
        refreshFavorites();
    }
}