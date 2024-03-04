package com.example.mherlsmanagementsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import functions.SalesAdapter;

public class AppInfo extends AppCompatActivity {
    TextView usernametext, RoleText;

    String username, role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);



        // This will get the intent from the previous page
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        role = intent.getStringExtra("role");


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
                Intent intent1 = new Intent(AppInfo.this, SystemDashboard.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role",role);
                startActivity(intent1);
                finish();

                // This will go to home or dashboard
            } else if (id == R.id.navigation_product) {

                // This will go to product class
                Intent intent1 = new Intent(AppInfo.this, Product.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role", role);
                startActivity(intent1);
                finish();


                // Handle navigation_product action
            } else if (id == R.id.navigation_notifications) {

                Intent intent1 = new Intent(AppInfo.this, SalesProducts.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role", role);
                startActivity(intent1);
                finish();



                // Handle navigation_notifications action
            } else if (id == R.id.create_user) {

                // This will go to add user
                if (role != null && role.equals("Admin")){

                    // This will go to add user
                    Intent intent1 = new Intent(AppInfo.this, AddUser.class);
                    intent1.putExtra("username", username);
                    intent1.putExtra("role", role);
                    startActivity(intent1);
                    finish();
                } else{
                    AlertDialog alerts = new AlertDialog.Builder(AppInfo.this).create();
                    alerts.setTitle("Alert");
                    alerts.setMessage("You are not an Admin, you can't access this page");
                    alerts.show();

                }

            }

            // This is for the app info
            else if (id == R.id.appinfo) {

                // alerts
                AlertDialog alerts = new AlertDialog.Builder(AppInfo.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the App Info Page");
                alerts.show();

            }
            else if (id == R.id.logoutid) {

                // This will ask the user to logout
                AlertDialog alert = new AlertDialog.Builder(AppInfo.this).create();
                alert.setTitle("Logout");
                alert.setMessage("Are you sure you want to logout?");
                alert.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {

                    // LOGOUT CLASS
                    Intent intent2 = new Intent(AppInfo.this, MainActivity.class);
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
            Intent intent = new Intent(AppInfo.this, MainActivity.class);
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