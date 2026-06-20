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
import com.example.a1222275_1220495_courseproject.adapters.UserAdapter;
import com.example.a1222275_1220495_courseproject.database.DataBaseHelper;
import com.example.a1222275_1220495_courseproject.models.User;

import java.util.List;

// Fragment to view users
public class ViewUsersFragment extends Fragment implements UserAdapter.OnUserDeleteListener {

    private RecyclerView rvUsers;
    private UserAdapter adapter;
    private DataBaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_view_users, container, false);

        dbHelper = new DataBaseHelper(requireContext());
        rvUsers = view.findViewById(R.id.rvUsers);
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        loadUsers();

        return view;
    }

    // Load users from db
    private void loadUsers() {
        List<User> userList = dbHelper.getAllUsers();
        if (adapter == null) {
            adapter = new UserAdapter(userList, this);
            rvUsers.setAdapter(adapter);
        } else {
            adapter.updateList(userList);
        }
    }

    // Delete user action
    @Override
    public void onDeleteClick(User user) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteUser(user.getId());
                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    loadUsers();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
