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

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private List<Trip> tripList;
    private OnTripClickListener listener;
    private List<Long> favoriteTripIds;

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
        void onFavoriteClick(Trip trip);
    }

    public TripAdapter(List<Trip> tripList, OnTripClickListener listener) {
        this.tripList = tripList;
        this.listener = listener;
    }

    public void setFavoriteTripIds(List<Long> favoriteTripIds) {
        this.favoriteTripIds = favoriteTripIds;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        boolean isFav = favoriteTripIds != null && favoriteTripIds.contains(trip.getTripId());
        holder.bind(trip, listener, isFav);
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public void updateList(List<Trip> newList) {
        this.tripList = newList;
        notifyDataSetChanged();
    }

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

        public void bind(final Trip trip, final OnTripClickListener listener, boolean isFavorite) {
            tvDestination.setText(trip.getDestination());
            tvCountry.setText(trip.getCountry());
            tvDuration.setText(trip.getDuration() + " days");
            tvPrice.setText("$" + trip.getPrice());
            tvRating.setText("★ " + trip.getRating());
            tvDescription.setText(trip.getDescription());

            // تغيير لون القلب بناءً على حالة المفضلة
            if (isFavorite) {
                imgFavorite.setImageResource(R.drawable.ic_favorites); // يمكنك استخدام أيقونة ملونة هنا
                imgFavorite.setImageTintList(android.content.res.ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.secondary_green)));
            } else {
                imgFavorite.setImageTintList(android.content.res.ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.light_gray)));
            }

            Glide.with(itemView.getContext())
                    .load(trip.getImage())
                    .placeholder(R.color.light_gray)
                    .into(imgTrip);

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onTripClick(trip);
            });

            imgFavorite.setOnClickListener(v -> {
                if (listener != null) listener.onFavoriteClick(trip);
            });
        }
    }
}
