package com.example.a1222275_1220495_courseproject.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1222275_1220495_courseproject.R;
import com.example.a1222275_1220495_courseproject.models.Reservation;

import java.util.List;

// Adapter for user reservations
public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private final List<Reservation> reservationList;

    public ReservationAdapter(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate reservation item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);

        // Set reservation details
        holder.tvDestination.setText(reservation.getTripDestination());
        holder.tvDate.setText(reservation.getReservationDate());
        holder.tvType.setText(reservation.getReservationType());
        holder.tvQuantity.setText(reservation.getQuantity() + " Persons");
        holder.tvStatus.setText(reservation.getStatus());

        // Apply status style
        applyStatusStyle(holder.tvStatus, reservation.getStatus());
    }

    // Status styling logic
    private void applyStatusStyle(TextView tvStatus, String status) {
        int color;
        int bgColor;

        if ("Confirmed".equalsIgnoreCase(status)) {
            color = Color.parseColor("#2FA084");
            bgColor = Color.parseColor("#E9F5F2");
        } else if ("Pending".equalsIgnoreCase(status)) {
            color = Color.parseColor("#757575");
            bgColor = Color.parseColor("#EEEEEE");
        } else if ("Cancelled".equalsIgnoreCase(status)) {
            color = Color.parseColor("#D32F2F");
            bgColor = Color.parseColor("#FFEBEE");
        } else {
            color = Color.BLACK;
            bgColor = Color.TRANSPARENT;
        }

        tvStatus.setTextColor(color);
        
        // Setup background
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(16);
        shape.setColor(bgColor);
        tvStatus.setBackground(shape);
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    // ViewHolder class
    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView tvDestination, tvDate, tvType, tvQuantity, tvStatus;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDestination = itemView.findViewById(R.id.tvTripDestination);
            tvStatus = itemView.findViewById(R.id.tvStatusBadge);
            tvDate = itemView.findViewById(R.id.tvReservationDate);
            tvType = itemView.findViewById(R.id.tvReservationType);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
}
