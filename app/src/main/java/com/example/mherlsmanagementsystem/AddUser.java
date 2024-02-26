package com.example.mherlsmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import FirebaseController.FirebaseController;
import functions.User;

public class AddUser extends AppCompatActivity implements UserAdapter.OnDeleteClickListener {

    FloatingActionButton add;
    TextView usernametext, RoleText;

    String username, role;

    RecyclerView recyclerView;

    private UserAdapter adapter;
    private List<User> userList;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        role = intent.getStringExtra("role");

        // Floating action button
        add = findViewById(R.id.floating_add_user);
        add.setOnClickListener(v -> {
            Intent intent1 = new Intent(AddUser.this, CreateUser.class);
            intent1.putExtra("username", username);
            intent1.putExtra("role", role);
            startActivity(intent1);
            finish();
        });

        // This will load the user
        LoadUser();

        //This is for recycleview
        // it has its function to display
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        adapter = new UserAdapter(userList);
        recyclerView.setAdapter(adapter);

        adapter.setOnDeleteClickListener(this); // Ensure MainActivity implements OnDeleteClickListener


        // This is for navbar
        NavigationView navigationView = findViewById(R.id.nav_view);
        usernametext = navigationView.getHeaderView(0).findViewById(R.id.username);
        RoleText = navigationView.getHeaderView(0).findViewById(R.id.Role);
        usernametext.setText("Username:" + username);
        RoleText.setText("Role:" + role);;

        // event listener of the navigation view
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {

                // Handle navigation_home action
                Intent intent1 = new Intent(AddUser.this, SystemDashboard.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role",role);
                startActivity(intent1);
                finish();


            } else if (id == R.id.navigation_product) {
                AlertDialog alertDialog = new AlertDialog.Builder(AddUser.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("You are already in the Product Page");
                alertDialog.show();
                // Handle navigation_product action
            } else if (id == R.id.navigation_notifications) {
                AlertDialog alerts = new AlertDialog.Builder(AddUser.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Notification Page");
                alerts.show();

                // Handle navigation_notifications action
            } else if (id == R.id.create_user) {

                AlertDialog alerts = new AlertDialog.Builder(AddUser.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Add User Page");
                alerts.show();
                // Handle create_user action

            }
            else if (id == R.id.appinfo) {
                AlertDialog alerts = new AlertDialog.Builder(AddUser.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Notification Page1");
                alerts.show();

            }
            else if (id == R.id.logoutid) {
                AlertDialog alert = new AlertDialog.Builder(AddUser.this).create();
                alert.setTitle("Logout");
                alert.setMessage("Are you sure you want to logout?");
                alert.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {

                    // LOGOUT CLASS
                    Intent intent2 = new Intent(AddUser.this, MainActivity.class);
                    startActivity(intent2);
                    finish();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();

                    // To sign out the current user
                    mAuth.signOut();
                });
                alert.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {
                    dialog.dismiss();
                });
                alert.show();
            }
            else {
                return false;
            }
            return true;
        });

    }
    public void LoadUser(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
          myRef = database.getReference("Users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();

                for (DataSnapshot complaintSnapshot : dataSnapshot.getChildren()) {
                    String Username = complaintSnapshot.child("username").getValue(String.class);
                    String Role = complaintSnapshot.child("role").getValue(String.class);
                    userList.add(new User("User" + Username, "Role" + Role));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AddUser.this, "Failed to read data from database", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onDeleteClick(int position) {
        // yes or no alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Complaint");
        builder.setMessage("Are you sure you want to delete this complaint?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            if (position >= 0 && position < userList.size()) {
                String user = usernametext.getText().toString();
                if (user.equals(username)) {
                    Toast.makeText(AddUser.this, "You can only delete your username ", Toast.LENGTH_SHORT).show();
                } else {
                    User deletedComplaint = userList.get(position);
                    deleteComplaintFromDatabase(deletedComplaint);
                    userList.remove(position);
                    adapter.notifyItemRemoved(position);
                }
            }
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Do nothing
        });
        builder.show();

    }

    private void deleteComplaintFromDatabase(User deletedComplaint) {
        String username = deletedComplaint.getUsername();

        Log.d("FirebaseDelete", "Deleting complaint with content: " +username);

        myRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("FirebaseDelete", "Found match, deleting: " + snapshot.getValue());
                    snapshot.getRef().removeValue();

                    Toast.makeText(AddUser.this, "User deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddUser.this, "Failed to delete complaint from database", Toast.LENGTH_SHORT).show();
            }
        });
    }
}