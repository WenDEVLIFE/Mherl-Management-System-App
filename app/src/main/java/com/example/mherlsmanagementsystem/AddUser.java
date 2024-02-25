package com.example.mherlsmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AddUser extends AppCompatActivity {

    FloatingActionButton add;
    TextView usernametext, RoleText;

    String username, role;

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
}