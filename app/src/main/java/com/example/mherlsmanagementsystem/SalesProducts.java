package com.example.mherlsmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import functions.ProductAdapter;
import functions.ProductBase;
import functions.Sales;
import functions.SalesAdapter;

public class SalesProducts extends AppCompatActivity implements SalesAdapter.OnDeleteClickListener{
    TextView usernametext, RoleText;

    String username, role;

    RecyclerView recyclerView;

    private SalesAdapter adapter;
    private List<Sales> salesList;

    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_products);


        // This will get the intent from the previous page
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        role = intent.getStringExtra("role");


        // it has its function to display
        recyclerView = findViewById(R.id.salesview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        salesList = new ArrayList<>();
        adapter = new SalesAdapter(salesList);
        recyclerView.setAdapter(adapter);

        // This is for the delete method
        adapter.setOnDeleteClickListener(this);

        // This is for the load user
        LoadUser();

        // This is for navbar
        NavigationView navigationView = findViewById(R.id.nav_view);
        usernametext = navigationView.getHeaderView(0).findViewById(R.id.username);
        RoleText = navigationView.getHeaderView(0).findViewById(R.id.Role);
        usernametext.setText("Username:" + username);
        RoleText.setText("Role:" + role);;

        // event listener of the navigation view
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            // This will go to home
            if (id == R.id.navigation_home) {

                // Handle navigation_home action
                Intent intent1 = new Intent(SalesProducts.this, SystemDashboard.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role",role);
                startActivity(intent1);
                finish();

                // This will go to home or dashboard
            } else if (id == R.id.navigation_product) {

                // This will go to product class
                Intent intent1 = new Intent(SalesProducts.this, Product.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role", role);
                startActivity(intent1);
                finish();

                // Handle navigation_product action
            } else if (id == R.id.navigation_notifications) {

                AlertDialog alerts = new AlertDialog.Builder(SalesProducts.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the sales Page");
                alerts.show();


                // Handle navigation_notifications action
            } else if (id == R.id.create_user) {

                AlertDialog alerts = new AlertDialog.Builder(SalesProducts.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Add User Page");
                alerts.show();
                // Handle create_user action

            }

            // This is for the app info
            else if (id == R.id.appinfo) {

                // alerts
                AlertDialog alerts = new AlertDialog.Builder(SalesProducts.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Notification Page1");
                alerts.show();

            }
            else if (id == R.id.logoutid) {

                // This will ask the user to logout
                AlertDialog alert = new AlertDialog.Builder(SalesProducts.this).create();
                alert.setTitle("Logout");
                alert.setMessage("Are you sure you want to logout?");
                alert.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {

                    // LOGOUT CLASS
                    Intent intent2 = new Intent(SalesProducts.this, MainActivity.class);
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

        // Load the products
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Sales");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                salesList.clear();

                // This will retrieve from the database
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String productname = userSnapshot.child("productname").getValue(String.class);
                    int price = userSnapshot.child("totalprice").getValue(int.class);
                    int quantity = userSnapshot.child("quantity").getValue(int.class);
                    String date = userSnapshot.child("date").getValue(String.class);
                    salesList.add(new Sales(productname, price, quantity, date));
                }

                // This is for the adapter
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

                // This will alert that it failed read on the database
                Toast.makeText(SalesProducts.this, "Failed to read data from database", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onDeleteClick(int position) {
        // yes or no alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Product?");
        builder.setMessage("Are you sure you want to delete this user?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            if (position >= 0 && position < salesList.size()) {

                // This is for the delete method
                Sales productdelete= salesList.get(position);
                deleteComplaintFromDatabase(productdelete);
                salesList.remove(position);
                adapter.notifyItemRemoved(position);

            }
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Do nothing
        });
        builder.show();
    }

    // This for delete method of user
    private void deleteComplaintFromDatabase(Sales info) {
        String productname = info.getProductName();

        Log.d("FirebaseDelete", "Deleting complaint with content: " +productname);

        myRef.orderByChild("productname").equalTo(productname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("FirebaseDelete", "Found match, deleting: " + snapshot.getValue());
                    snapshot.getRef().removeValue();

                    Toast.makeText(SalesProducts.this, "Sales deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SalesProducts.this, "Failed to delete sales from database", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(SalesProducts.this, MainActivity.class);
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
}