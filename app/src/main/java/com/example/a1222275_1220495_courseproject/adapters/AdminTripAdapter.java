package com.example.a1222275_1220495_courseproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a1222275_1220495_courseproject.R;
import com.example.a1222275_1220495_courseproject.models.Trip;
import com.google.android.material.button.MaterialButton;

import java.util.List;

// Adapter for admin trips
public class AdminTripAdapter extends RecyclerView.Adapter<AdminTripAdapter.AdminTripViewHolder> {

    private List<Trip> tripList;
    private OnTripActionListener listener;

    // Interface for trip actions
    public interface OnTripActionListener {
        void onEdit(Trip trip);
        void onDelete(Trip trip);
    }

    public AdminTripAdapter(List<Trip> tripList, OnTripActionListener listener) {
        this.tripList = tripList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminTripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate trip item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_trip, parent, false);
        return new AdminTripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminTripViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        // Set trip info
        holder.tvDestination.setText(trip.getDestination());
        holder.tvCountry.setText(trip.getCountry());
        holder.tvPrice.setText("$" + trip.getPrice());
        holder.tvRating.setText(String.valueOf(trip.getRating()));
        holder.tvDuration.setText(trip.getDuration() + " Days");
        holder.tvDescription.setText(trip.getDescription());

        // Load trip image
        Glide.with(holder.itemView.getContext())
                .load(trip.getImage())
                .placeholder(R.drawable.travel_logo1)
                .error(R.drawable.travel_logo1)
                .into(holder.ivTripImage);

        // Edit and Delete buttons
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(trip));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(trip));
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    // Refresh list data
    public void updateList(List<Trip> newList) {
        this.tripList = newList;
        notifyDataSetChanged();
    }

    // ViewHolder class
    static class AdminTripViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTripImage;
        TextView tvDestination, tvCountry, tvPrice, tvRating, tvDuration, tvDescription;
        MaterialButton btnEdit, btnDelete;

        public AdminTripViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTripImage = itemView.findViewById(R.id.ivAdminTripImage);
            tvDestination = itemView.findViewById(R.id.tvAdminTripDestination);
            tvCountry = itemView.findViewById(R.id.tvAdminTripCountry);
            tvPrice = itemView.findViewById(R.id.tvAdminTripPrice);
            tvRating = itemView.findViewById(R.id.tvAdminTripRating);
            tvDuration = itemView.findViewById(R.id.tvAdminTripDuration);
            tvDescription = itemView.findViewById(R.id.tvAdminTripDescription);
            btnEdit = itemView.findViewById(R.id.btnAdminEditTrip);
            btnDelete = itemView.findViewById(R.id.btnAdminDeleteTrip);
        }
    }
}
