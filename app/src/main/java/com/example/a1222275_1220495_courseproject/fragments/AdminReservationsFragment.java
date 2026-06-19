package com.example.a1222275_1220495_courseproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1222275_1220495_courseproject.R;
import com.example.a1222275_1220495_courseproject.adapters.AdminReservationAdapter;
import com.example.a1222275_1220495_courseproject.database.DataBaseHelper;
import com.example.a1222275_1220495_courseproject.models.AdminReservation;

import java.util.List;

public class AdminReservationsFragment extends Fragment implements AdminReservationAdapter.OnReservationActionListener {

    private RecyclerView rvReservations;
    private AdminReservationAdapter adapter;
    private DataBaseHelper dbHelper;
    private LinearLayout emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_reservations, container, false);

        try {
            dbHelper = new DataBaseHelper(requireContext());
            rvReservations = view.findViewById(R.id.rvAdminReservations);
            emptyView = view.findViewById(R.id.emptyViewAdminRes);
            rvReservations.setLayoutManager(new LinearLayoutManager(getContext()));

            loadReservations();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Initialization Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void loadReservations() {
        try {
            List<AdminReservation> reservations = dbHelper.getAllAdminReservations();
            if (reservations.isEmpty()) {
                rvReservations.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                rvReservations.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                if (adapter == null) {
                    adapter = new AdminReservationAdapter(reservations, this);
                    rvReservations.setAdapter(adapter);
                } else {
                    adapter.updateList(reservations);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error loading reservations: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdateStatus(AdminReservation reservation) {
        String[] statuses = {"Pending", "Confirmed", "Cancelled"};
        
        new AlertDialog.Builder(requireContext())
                .setTitle("Update Reservation Status")
                .setItems(statuses, (dialog, which) -> {
                    try {
                        String selectedStatus = statuses[which];
                        dbHelper.updateReservationStatus(reservation.getReservationId(), selectedStatus);
                        Toast.makeText(getContext(), "Status updated to " + selectedStatus, Toast.LENGTH_SHORT).show();
                        loadReservations();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    @Override
    public void onDelete(AdminReservation reservation) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Reservation")
                .setMessage("Are you sure you want to delete this reservation?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    try {
                        dbHelper.deleteReservation(reservation.getReservationId());
                        Toast.makeText(getContext(), "Reservation deleted successfully", Toast.LENGTH_SHORT).show();
                        loadReservations();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Delete failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
