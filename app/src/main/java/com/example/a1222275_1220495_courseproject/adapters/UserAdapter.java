package com.example.a1222275_1220495_courseproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1222275_1220495_courseproject.R;
import com.example.a1222275_1220495_courseproject.models.User;

import java.util.List;

// Adapter for users list
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private OnUserDeleteListener deleteListener;

    // Interface for delete action
    public interface OnUserDeleteListener {
        void onDeleteClick(User user);
    }

    public UserAdapter(List<User> userList, OnUserDeleteListener deleteListener) {
        this.userList = userList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate user item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        // Set user details
        holder.tvFullName.setText(user.getFirstName() + " " + user.getLastName());
        holder.tvEmail.setText(user.getEmail());
        holder.tvPhone.setText(user.getPhone());
        holder.tvCategory.setText(user.getCategory());

        // Handle delete click
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Update the list
    public void updateList(List<User> newList) {
        this.userList = newList;
        notifyDataSetChanged();
    }

    // View holder for user items
    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvFullName, tvEmail, tvPhone, tvCategory;
        ImageButton btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvUserFullName);
            tvEmail = itemView.findViewById(R.id.tvUserEmail);
            tvPhone = itemView.findViewById(R.id.tvUserPhone);
            tvCategory = itemView.findViewById(R.id.tvUserCategory);
            btnDelete = itemView.findViewById(R.id.btnDeleteUser);
        }
    }
}
