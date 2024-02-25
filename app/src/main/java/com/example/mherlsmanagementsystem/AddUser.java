package com.example.mherlsmanagementsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class AddUser extends AppCompatActivity {

    FloatingActionButton add;
    TextView usernametext, RoleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String role = intent.getStringExtra("role");

        // Floating action button
        add = findViewById(R.id.floating_add_user);
        add.setOnClickListener(v -> {
            Intent intent1 = new Intent(AddUser.this, CreateUser.class);
            startActivity(intent1);
            intent1.putExtra("username", username);
            intent1.putExtra("role", role);
            finish();
        });



        NavigationView navigationView = findViewById(R.id.nav_view);
        usernametext = navigationView.getHeaderView(0).findViewById(R.id.username);
        usernametext.setText("Username:" + username);
        RoleText.setText("Role:" + role);;

        // event listener of the navigation view
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {

                // Handle navigation_home action
                Intent intent1 = new Intent(AddUser.this, SystemDashboard.class);
                startActivity(intent1);
                intent1.putExtra("username", username);
                intent1.putExtra("role", role);
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
                alerts.setMessage("You are already in the Notification Page");
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
                    Intent intent2 = new Intent(AddUser.this, MainActivity.class);
                    startActivity(intent2);
                    intent2.putExtra("username", username);
                    intent2.putExtra("role", role);
                    finish();
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