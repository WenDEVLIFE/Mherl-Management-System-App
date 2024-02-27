package com.example.mherlsmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

import functions.ProductAdapter;
import functions.ProductBase;
import functions.User;
import functions.UserAdapter;

public class Product extends AppCompatActivity implements ProductAdapter.OnDeleteClickListener, ProductAdapter.OnEditClickListener {
    TextView usernametext, RoleText;

    FloatingActionButton addproducts;

    RecyclerView recyclerView;

    private ProductAdapter adapter;
    private List<ProductBase> productlist;

    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // it has its function to display
        recyclerView = findViewById(R.id.productviewer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productlist = new ArrayList<>();
        adapter = new ProductAdapter(productlist);
        recyclerView.setAdapter(adapter);

        adapter.setOnDeleteClickListener(this); // Ensure MainActivity implements OnDeleteClickListener
        adapter.setOnEditClickListener(this);

        // Load the product
        LoadUser();

        // Get the intent from the previous activity
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String role = intent.getStringExtra("role");

        // This is for floating button actions
        addproducts = findViewById(R.id.floating_add_product);
        addproducts.setOnClickListener(v -> {

            // This method is for adding products
            Intent intent1 = new Intent(Product.this, CreateProducts.class);
            intent1.putExtra( "username", username);
            intent1.putExtra("role", role);
            startActivity(intent1);


        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        usernametext = navigationView.getHeaderView(0).findViewById(R.id.username);
        RoleText = navigationView.getHeaderView(0).findViewById(R.id.Role);
        usernametext.setText("Username:" + username);
        RoleText.setText("Role:" + role);

        // event listener of the navigation view
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {

                // Handle navigation_home action
                AlertDialog alertDialog = new AlertDialog.Builder(Product.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("You are already in the Home Page");
                alertDialog.show();


            } else if (id == R.id.navigation_product) {

                // alerts
                AlertDialog alerts = new AlertDialog.Builder(Product.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Product page");
                alerts.show();
                // Handle navigation_product action
            } else if (id == R.id.navigation_notifications) {

                // alerts
                AlertDialog alerts = new AlertDialog.Builder(Product.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Notification Page");
                alerts.show();

                // Handle navigation_notifications action
            } else if (id == R.id.create_user) {

                // This will go to add user
                AlertDialog alerts = new AlertDialog.Builder(Product.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Notification Page");
                alerts.show();
                // Handle create_user action
                Intent intent1 = new Intent(Product.this, AddUser.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role", role);
                startActivity(intent1);
                finish();
            }
            else if (id == R.id.appinfo) {

                AlertDialog alerts = new AlertDialog.Builder(Product.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Notification Page1");
                alerts.show();

            }
            else if (id == R.id.logoutid) {

                // This is logout action
                AlertDialog alert = new AlertDialog.Builder(Product.this).create();
                alert.setTitle("Logout");
                alert.setMessage("Are you sure you want to logout?");
                alert.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
                    Intent intent2 = new Intent(Product.this, MainActivity.class);
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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Products");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productlist.clear();

                // This will retrieve from the database
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                     String productname = userSnapshot.child("productname").getValue(String.class);
                     String price = userSnapshot.child("price").getValue(String.class);
                     int quantity = userSnapshot.child("quantity").getValue(Integer.class);
                     productlist.add(new ProductBase(productname, quantity, price));
                }

                // This is for the adapter
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Product.this, "Failed to read data from database", Toast.LENGTH_SHORT).show();
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
            if (position >= 0 && position < productlist.size()) {
                ProductBase productdelete= productlist.get(position);
                deleteComplaintFromDatabase(productdelete);
                productlist.remove(position);
                adapter.notifyItemRemoved(position);

            }
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Do nothing
        });
        builder.show();
    }

    // This for delete method of user
    private void deleteComplaintFromDatabase(ProductBase info) {
        String productname = info.getProductname();

        Log.d("FirebaseDelete", "Deleting complaint with content: " +productname);

        myRef.orderByChild("productname").equalTo(productname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("FirebaseDelete", "Found match, deleting: " + snapshot.getValue());
                    snapshot.getRef().removeValue();

                    Toast.makeText(Product.this, "Product deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Product.this, "Failed to delete username from database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEditClick(int position) {

        // This is for the edit button
        ProductBase product = productlist.get(position);
        Intent intent = new Intent(Product.this, SystemDashboard.class);
        intent.putExtra("productname", product.getProductname());
        intent.putExtra("price", product.getPrice());
        intent.putExtra("quantity", product.getQuantity());
        startActivity(intent);



    }
}