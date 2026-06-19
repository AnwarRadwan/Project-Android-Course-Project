package com.example.a1222275_1220495_courseproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.a1222275_1220495_courseproject.models.Reservation;

import java.util.List;

public class ViewReservationsFragment extends Fragment implements AdminReservationAdapter.OnReservationActionListener {

    private RecyclerView rvReservations;
    private AdminReservationAdapter adapter;
    private DataBaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_reservations, container, false);

        dbHelper = new DataBaseHelper(requireContext());
        rvReservations = view.findViewById(R.id.rvAdminReservations);
        rvReservations.setLayoutManager(new LinearLayoutManager(getContext()));

        loadReservations();

        return view;
    }

    private void loadReservations() {
        List<Reservation> reservations = dbHelper.getAllReservations();
        if (adapter == null) {
            adapter = new AdminReservationAdapter(reservations, this);
            rvReservations.setAdapter(adapter);
        } else {
            adapter.updateList(reservations);
        }
    }

    @Override
    public void onUpdateStatus(Reservation reservation) {
        String[] statuses = {"Pending", "Confirmed", "Cancelled"};
        
        new AlertDialog.Builder(requireContext())
                .setTitle("Update Reservation Status")
                .setItems(statuses, (dialog, which) -> {
                    String selectedStatus = statuses[which];
                    dbHelper.updateReservationStatus(reservation.getReservationId(), selectedStatus);
                    Toast.makeText(getContext(), "Status updated to " + selectedStatus, Toast.LENGTH_SHORT).show();
                    loadReservations();
                })
                .show();
    }

    @Override
    public void onDelete(Reservation reservation) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Reservation")
                .setMessage("Are you sure you want to delete this reservation?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteReservation(reservation.getReservationId());
                    Toast.makeText(getContext(), "Reservation deleted successfully", Toast.LENGTH_SHORT).show();
                    loadReservations();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
