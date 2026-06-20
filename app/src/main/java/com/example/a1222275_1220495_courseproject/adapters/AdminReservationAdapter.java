package com.example.a1222275_1220495_courseproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1222275_1220495_courseproject.R;
import com.example.a1222275_1220495_courseproject.models.AdminReservation;
import com.google.android.material.button.MaterialButton;

import java.util.List;

// Adapter for admin reservations
public class AdminReservationAdapter extends RecyclerView.Adapter<AdminReservationAdapter.AdminResViewHolder> {

    private List<AdminReservation> reservationList;
    private OnReservationActionListener listener;

    // Interface for actions
    public interface OnReservationActionListener {
        void onUpdateStatus(AdminReservation reservation);
        void onDelete(AdminReservation reservation);
    }

    public AdminReservationAdapter(List<AdminReservation> reservationList, OnReservationActionListener listener) {
        this.reservationList = reservationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminResViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_reservation, parent, false);
        return new AdminResViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminResViewHolder holder, int position) {
        AdminReservation reservation = reservationList.get(position);
        // Set data
        holder.tvTripName.setText(reservation.getTripName());
        holder.tvStatus.setText(reservation.getStatus());
        holder.tvUserName.setText("User: " + reservation.getUserName());
        holder.tvDate.setText("Date: " + reservation.getReservationDate());
        holder.tvQuantity.setText("Qty: " + reservation.getQuantity());
        holder.tvType.setText("Type: " + reservation.getReservationType());

        // Update click
        holder.btnUpdateStatus.setOnClickListener(v -> listener.onUpdateStatus(reservation));
        // Delete click
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(reservation));
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    // Refresh data
    public void updateList(List<AdminReservation> newList) {
        this.reservationList = newList;
        notifyDataSetChanged();
    }

    // ViewHolder class
    static class AdminResViewHolder extends RecyclerView.ViewHolder {
        TextView tvTripName, tvStatus, tvUserName, tvDate, tvQuantity, tvType;
        MaterialButton btnUpdateStatus, btnDelete;

        public AdminResViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTripName = itemView.findViewById(R.id.tvAdminResTripName);
            tvStatus = itemView.findViewById(R.id.tvAdminResStatus);
            tvUserName = itemView.findViewById(R.id.tvAdminResUserName);
            tvDate = itemView.findViewById(R.id.tvAdminResDate);
            tvQuantity = itemView.findViewById(R.id.tvAdminResQuantity);
            tvType = itemView.findViewById(R.id.tvAdminResType);
            btnUpdateStatus = itemView.findViewById(R.id.btnAdminUpdateResStatus);
            btnDelete = itemView.findViewById(R.id.btnAdminDeleteRes);
        }
    }
}
