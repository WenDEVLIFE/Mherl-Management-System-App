package com.example.mherlsmanagementsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import functions.User;
import functions.UserAdapter;

public class AddUser extends AppCompatActivity implements UserAdapter.OnDeleteClickListener, GestureDetector.OnGestureListener {

    FloatingActionButton add;
    TextView usernametext, RoleText;

    String username, role;

    RecyclerView recyclerView;

    private UserAdapter adapter;
    private List<User> userList;
    DatabaseReference myRef;

    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // This will get the intent from the previous page
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        role = intent.getStringExtra("role");

        // Floating action button
        add = findViewById(R.id.floating_add_user);
        add.setOnClickListener(v -> {

            // This will go to create user class
            Intent intent1 = new Intent(AddUser.this, CreateUser.class);
            intent1.putExtra("username", username);
            intent1.putExtra("role", role);
            startActivity(intent1);
            finish();

                    });

        // This will load the user
        LoadUser();

        //This is for recycle view
        // it has its function to display
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        adapter = new UserAdapter(userList);
        recyclerView.setAdapter(adapter);

        adapter.setOnDeleteClickListener(this); // Ensure MainActivity implements OnDeleteClickListener
        gestureDetector = new GestureDetector(this, this);

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

                // This will go to home or dashboard
            } else if (id == R.id.navigation_product) {

                // This will go to product class
                Intent intent1 = new Intent(AddUser.this, Product.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role", role);
                startActivity(intent1);
                finish();

                // Handle navigation_product action
            } else if (id == R.id.navigation_notifications) {

                // Go to sales
                Intent intent1 = new Intent(AddUser.this, SalesProducts.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role", role);
                startActivity(intent1);
                finish();



                // Handle navigation_notifications action
            } else if (id == R.id.create_user) {

                AlertDialog alerts = new AlertDialog.Builder(AddUser.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Add User Page");
                alerts.show();
                // Handle create_user action

            }

            // This is for the app info
            else if (id == R.id.appinfo) {

                // This will go to app info
                Intent intent1 = new Intent(AddUser.this, AppInfo.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role", role);
                startActivity(intent1);
                finish();

            }
            else if (id == R.id.logoutid) {

                // This will ask the user to logout
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

    // This is where we load the user from the database to recycleviewer
    public void LoadUser(){

        // This is for the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // This is for the reference
        myRef = database.getReference("Users");

        // This is for the event listener
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();

                // This will retrieve from the database
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String Username = userSnapshot.child("username").getValue(String.class);
                    String Role = userSnapshot.child("role").getValue(String.class);
                    userList.add(new User(Username, Role)); // Removed "User" and "Role" prefix
                }

                // This is for the adapter
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
        builder.setTitle("Delete User");
        builder.setMessage("Are you sure you want to delete this user?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            if (position >= 0 && position < userList.size()) {
                User userToDelete = userList.get(position);
                String userToDeleteUsername = userToDelete.getUsername();

                if (userToDeleteUsername.equals(username)) {
                    Toast.makeText(AddUser.this, "You can only delete your username ", Toast.LENGTH_SHORT).show();
                } else {
                    deleteComplaintFromDatabase(userToDelete);
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

    // This for delete method of user
    private void deleteComplaintFromDatabase(User info) {
        String usernamedb = info.getUsername();

        Log.d("FirebaseDelete", "Deleting complaint with content: " +usernamedb);

        myRef.orderByChild("username").equalTo(usernamedb).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("FirebaseDelete", "Found match, deleting: " + snapshot.getValue());
                    snapshot.getRef().removeValue();

                    LocalDate date = null;
                    LocalTime time = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        // This is for the report
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reports = database.getReference("Reports");
                        String ReportId = UUID.randomUUID().toString();
                        date = LocalDate.now();
                        time = LocalTime.now();
                        String dateformat = date.toString();
                        String timeformat = time.toString();

                        Map <String, Object> report = new HashMap<>();
                        report.put("username", username);
                        report.put("Activity", "Deleted User");
                        report.put("Date", dateformat);
                        report.put("Time", timeformat);
                        reports.child(ReportId).setValue(report);
                    }

                    Toast.makeText(AddUser.this, "User deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddUser.this, "Failed to delete username from database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // This method is to handle the back button
    // we add Supper.onBackPressed() to handle the back button
    // Override the onBackPressed method
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // LOGOUT CLASS
            Intent intent = new Intent(AddUser.this, MainActivity.class);
            startActivity(intent);
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            // To sign out the current user
            mAuth.signOut();
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Pass touch events to the GestureDetector
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    // This is for gesture
    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Check for upward swipe and reload the activity
        assert e1 != null;
        if (e1.getY() > e2.getY()) {
            reloadActivity();
            return true;
        }
        return false;
    }

    // Function to reload the activity
    private void reloadActivity() {
        recreate();
    }
}