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

// Fragment to view all reservations
public class ViewReservationsFragment extends Fragment implements AdminReservationAdapter.OnReservationActionListener {

    private RecyclerView rvReservations;
    private AdminReservationAdapter adapter;
    private DataBaseHelper dbHelper;
    private LinearLayout emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_admin_reservations, container, false);

        dbHelper = new DataBaseHelper(requireContext());
        rvReservations = view.findViewById(R.id.rvAdminReservations);
        emptyView = view.findViewById(R.id.emptyViewAdminRes);
        rvReservations.setLayoutManager(new LinearLayoutManager(getContext()));

        loadReservations();

        return view;
    }

    // Load reservations from db
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
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Update status action
    @Override
    public void onUpdateStatus(AdminReservation reservation) {
        String[] statuses = {"Pending", "Confirmed", "Cancelled"};
        
        new AlertDialog.Builder(requireContext())
                .setTitle("Update Status")
                .setItems(statuses, (dialog, which) -> {
                    String selectedStatus = statuses[which];
                    dbHelper.updateReservationStatus(reservation.getReservationId(), selectedStatus);
                    Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                    loadReservations();
                })
                .show();
    }

    // Delete reservation action
    @Override
    public void onDelete(AdminReservation reservation) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteReservation(reservation.getReservationId());
                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    loadReservations();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
