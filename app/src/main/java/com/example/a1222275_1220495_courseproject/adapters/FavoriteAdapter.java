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

// Adapter for favorite trips
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<Trip> favoriteList;
    private OnFavoriteActionListener listener;

    // Listener interface
    public interface OnFavoriteActionListener {
        void onRemove(Trip trip, int position);
        void onReserve(Trip trip);
    }

    public FavoriteAdapter(List<Trip> favoriteList, OnFavoriteActionListener listener) {
        this.favoriteList = favoriteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Trip trip = favoriteList.get(position);

        // Set data
        holder.tvDestination.setText(trip.getDestination());
        holder.tvCountry.setText(trip.getCountry());
        holder.tvPrice.setText("$" + trip.getPrice());
        holder.tvRating.setText("★ " + trip.getRating());

        // Load image
        Glide.with(holder.itemView.getContext())
                .load(trip.getImage())
                .placeholder(R.color.light_gray)
                .centerCrop()
                .into(holder.imgTrip);

        // Remove click
        holder.btnRemove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemove(trip, position);
            }
        });

        // Reserve click
        holder.btnReserve.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReserve(trip);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    // ViewHolder class
    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTrip;
        TextView tvDestination, tvCountry, tvPrice, tvRating;
        MaterialButton btnRemove, btnReserve;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTrip = itemView.findViewById(R.id.imgFavoriteTrip);
            tvDestination = itemView.findViewById(R.id.tvFavDestination);
            tvCountry = itemView.findViewById(R.id.tvFavCountry);
            tvPrice = itemView.findViewById(R.id.tvFavPrice);
            tvRating = itemView.findViewById(R.id.tvFavRating);
            btnRemove = itemView.findViewById(R.id.btnRemoveFav);
            btnReserve = itemView.findViewById(R.id.btnReserveFav);
        }
    }
}
