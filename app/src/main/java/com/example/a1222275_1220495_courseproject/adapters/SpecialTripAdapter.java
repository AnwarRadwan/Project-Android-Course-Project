package com.example.a1222275_1220495_courseproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a1222275_1220495_courseproject.R;
import com.example.a1222275_1220495_courseproject.database.DataBaseHelper;
import com.example.a1222275_1220495_courseproject.models.Trip;
import com.google.android.material.button.MaterialButton;

import java.util.List;

// Adapter for special trips section
public class SpecialTripAdapter extends RecyclerView.Adapter<SpecialTripAdapter.ViewHolder> {

    private List<Trip> trips;
    private Context context;
    private long userId;
    private OnTripClickListener clickListener;
    private DataBaseHelper dbHelper;

    // Interface for clicks
    public interface OnTripClickListener {
        void onViewDetails(Trip trip);
    }

    public SpecialTripAdapter(Context context, List<Trip> trips, long userId, OnTripClickListener clickListener) {
        this.context = context;
        this.trips = trips;
        this.userId = userId;
        this.clickListener = clickListener;
        this.dbHelper = new DataBaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate special trip layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_special_trip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = trips.get(position);

        // Populate details
        holder.tvDestination.setText(trip.getDestination());
        holder.tvCountry.setText(trip.getCountry());
        holder.tvDuration.setText(trip.getDuration() + " Days");
        holder.tvPrice.setText("$" + trip.getPrice());
        holder.tvRating.setText("Rating: " + trip.getRating());
        holder.tvDescription.setText(trip.getDescription());

        // Load image with Glide
        Glide.with(context)
                .load(trip.getImage())
                .placeholder(R.color.light_gray)
                .into(holder.imgTrip);

        // Details click
        holder.btnViewDetails.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onViewDetails(trip);
            }
        });

        // Add to favorite
        holder.btnAddFavorite.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean added = dbHelper.addFavorite(userId, trip.getTripId());
            if (added) {
                Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Already in Favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    // View holder for special trips
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTrip;
        TextView tvDestination, tvCountry, tvDuration, tvPrice, tvRating, tvDescription;
        MaterialButton btnViewDetails, btnAddFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTrip = itemView.findViewById(R.id.imgTrip);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            btnAddFavorite = itemView.findViewById(R.id.btnAddFavorite);
        }
    }
}
