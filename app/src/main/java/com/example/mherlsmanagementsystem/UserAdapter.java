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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ComplaintViewHolder> {

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
    public ComplaintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.useritem, parent, false);
        return new ComplaintViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintViewHolder holder, int position) {
        User complaint = userlist.get(position);

        holder.bind(complaint);
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    // ViewHolder class
    public class ComplaintViewHolder extends RecyclerView.ViewHolder {

        // Views in your item layout
        private final TextView Username;
        private final TextView Role;

        private final Button deleteButton;

        public ComplaintViewHolder(@NonNull View itemView) {
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

        public void bind(User complaint) {
            // Bind data to views
            Username.setText(complaint.Username());
            Role.setText(complaint.Role());

        }
    }
}
