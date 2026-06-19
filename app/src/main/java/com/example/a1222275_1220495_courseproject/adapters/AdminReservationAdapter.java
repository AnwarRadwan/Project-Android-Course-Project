package com.example.a1222275_1220495_courseproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1222275_1220495_courseproject.R;
import com.example.a1222275_1220495_courseproject.models.Reservation;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AdminReservationAdapter extends RecyclerView.Adapter<AdminReservationAdapter.AdminResViewHolder> {

    private List<Reservation> reservationList;
    private OnReservationActionListener listener;

    public interface OnReservationActionListener {
        void onUpdateStatus(Reservation reservation);
        void onDelete(Reservation reservation);
    }

    public AdminReservationAdapter(List<Reservation> reservationList, OnReservationActionListener listener) {
        this.reservationList = reservationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminResViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_reservation, parent, false);
        return new AdminResViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminResViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        holder.tvTripName.setText(reservation.getTripDestination());
        holder.tvStatus.setText(reservation.getStatus());
        holder.tvUserEmail.setText(reservation.getUserEmail());
        holder.tvDate.setText("Date: " + reservation.getReservationDate());
        holder.tvQuantity.setText("Qty: " + reservation.getQuantity());

        holder.btnUpdateStatus.setOnClickListener(v -> listener.onUpdateStatus(reservation));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(reservation));
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public void updateList(List<Reservation> newList) {
        this.reservationList = newList;
        notifyDataSetChanged();
    }

    static class AdminResViewHolder extends RecyclerView.ViewHolder {
        TextView tvTripName, tvStatus, tvUserEmail, tvDate, tvQuantity;
        MaterialButton btnUpdateStatus, btnDelete;

        public AdminResViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTripName = itemView.findViewById(R.id.tvAdminResTripName);
            tvStatus = itemView.findViewById(R.id.tvAdminResStatus);
            tvUserEmail = itemView.findViewById(R.id.tvAdminResUserEmail);
            tvDate = itemView.findViewById(R.id.tvAdminResDate);
            tvQuantity = itemView.findViewById(R.id.tvAdminResQuantity);
            btnUpdateStatus = itemView.findViewById(R.id.btnAdminUpdateResStatus);
            btnDelete = itemView.findViewById(R.id.btnAdminDeleteRes);
        }
    }
}
