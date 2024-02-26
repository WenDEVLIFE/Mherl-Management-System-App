package com.example.mherlsmanagementsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class CreateProducts extends AppCompatActivity {
    TextView usernametext, RoleText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_products);



        // Get the intent from the previous activity
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String role = intent.getStringExtra("role");


        // Our navagation view
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
                AlertDialog alertDialog = new AlertDialog.Builder(CreateProducts.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("You are already in the Home Page");
                alertDialog.show();


            } else if (id == R.id.navigation_product) {

                // alerts
                AlertDialog alerts = new AlertDialog.Builder(CreateProducts.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Product page");
                alerts.show();
                // Handle navigation_product action
            } else if (id == R.id.navigation_notifications) {

                // alerts
                AlertDialog alerts = new AlertDialog.Builder(CreateProducts.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Notification Page");
                alerts.show();

                // Handle navigation_notifications action
            } else if (id == R.id.create_user) {

                // This will go to add user
                AlertDialog alerts = new AlertDialog.Builder(CreateProducts.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Notification Page");
                alerts.show();
                // Handle create_user action
                Intent intent1 = new Intent(CreateProducts.this, AddUser.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role", role);
                startActivity(intent1);
                finish();
            }
            else if (id == R.id.appinfo) {

                AlertDialog alerts = new AlertDialog.Builder(CreateProducts.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Notification Page1");
                alerts.show();

            }
            else if (id == R.id.logoutid) {

                // This is logout action
                AlertDialog alert = new AlertDialog.Builder(CreateProducts.this).create();
                alert.setTitle("Logout");
                alert.setMessage("Are you sure you want to logout?");
                alert.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
                    Intent intent2 = new Intent(CreateProducts.this, MainActivity.class);
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

}