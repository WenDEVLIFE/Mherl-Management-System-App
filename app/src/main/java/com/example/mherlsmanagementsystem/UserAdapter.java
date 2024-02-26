package com.example.mherlsmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import functions.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userlist;
    private OnDeleteClickListener onDeleteClickListener;

    public UserAdapter(List<User> userlist) {
        this.userlist = userlist;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.useritem, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User info = userlist.get(position);

        holder.bind(info);
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    // ViewHolder class
    public class UserViewHolder extends RecyclerView.ViewHolder {

        // Views in your item layout
        private final TextView Username;
        private final TextView Role;

        private final Button deleteButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            Username = itemView.findViewById(R.id.complaintNameTextView);
            Role = itemView.findViewById(R.id.complaintTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);


            // Set click listener for delete button
            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(position);
                }
            });
        }

        public void bind(User info) {
            // Bind data to views
            Username.setText(info.Username());
            Role.setText(info.Role());

        }
    }
}
