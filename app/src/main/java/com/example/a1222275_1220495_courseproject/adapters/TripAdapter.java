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
import java.util.List;

// Trip adapter class
public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private List<Trip> tripList;
    private OnTripClickListener listener;
    private List<Long> favoriteTripIds;

    // Interface for click events
    public interface OnTripClickListener {
        void onTripClick(Trip trip);
        void onFavoriteClick(Trip trip);
    }

    public TripAdapter(List<Trip> tripList, OnTripClickListener listener) {
        this.tripList = tripList;
        this.listener = listener;
    }

    // Update favorites
    public void setFavoriteTripIds(List<Long> favoriteTripIds) {
        this.favoriteTripIds = favoriteTripIds;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        boolean isFav = favoriteTripIds != null && favoriteTripIds.contains(trip.getTripId());
        // Bind data
        holder.bind(trip, listener, isFav);
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    // Refresh list
    public void updateList(List<Trip> newList) {
        this.tripList = newList;
        notifyDataSetChanged();
    }

    // ViewHolder class
    static class TripViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTrip, imgFavorite;
        TextView tvDestination, tvCountry, tvDuration, tvPrice, tvRating, tvDescription;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTrip = itemView.findViewById(R.id.imgTrip);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        // Bind trip data
        public void bind(final Trip trip, final OnTripClickListener listener, boolean isFavorite) {
            tvDestination.setText(trip.getDestination());
            tvCountry.setText(trip.getCountry());
            tvDuration.setText(trip.getDuration() + " days");
            tvPrice.setText("$" + trip.getPrice());
            tvRating.setText("★ " + trip.getRating());
            tvDescription.setText(trip.getDescription());

            // Set favorite icon
            if (isFavorite) {
                imgFavorite.setImageResource(R.drawable.ic_favorites); 
                imgFavorite.setImageTintList(android.content.res.ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.secondary_green)));
            } else {
                imgFavorite.setImageTintList(android.content.res.ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.light_gray)));
            }

            // Load image
            Glide.with(itemView.getContext())
                    .load(trip.getImage())
                    .placeholder(R.color.light_gray)
                    .into(imgTrip);

            // Item click
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onTripClick(trip);
            });

            // Favorite click
            imgFavorite.setOnClickListener(v -> {
                if (listener != null) listener.onFavoriteClick(trip);
            });
        }
    }
}
