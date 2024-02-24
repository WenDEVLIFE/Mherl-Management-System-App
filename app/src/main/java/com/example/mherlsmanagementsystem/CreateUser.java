package com.example.mherlsmanagementsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Observable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class CreateUser extends AppCompatActivity {
    private TextView usernametext;

    private Button adduser, back;

    private EditText username, password;

    private Spinner role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);


        // UsernameField and PasswordField
        username = findViewById(R.id.usernamefield);
        password = findViewById(R.id.passwordfield);


        // This is for spinner
        role = findViewById(R.id.spinner);
        role.setPrompt("Select Role");
        ArrayList<String> roles = new ArrayList<>();
        roles.add("Select Role");
        roles.add("Admin");
        roles.add("User");

        // This is for the spinner adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role.setAdapter(adapter);

        // This is for the add user button
        adduser= findViewById(R.id.buttonadd);
        adduser.setOnClickListener(v -> {

            AlertDialog alertDialog = new AlertDialog.Builder(CreateUser.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("User Added Successfully");
            alertDialog.show();
        });

        // This is for the back button
        back = findViewById(R.id.buttonback);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(CreateUser.this, AddUser.class);
            startActivity(intent);
            finish();
        });



        NavigationView navigationView = findViewById(R.id.nav_view);
        usernametext = navigationView.getHeaderView(0).findViewById(R.id.username);
        usernametext.setText("Username: AdminUser");

        // event listener of the navigation view
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {

                // Handle navigation_home action
                Intent intent = new Intent(CreateUser.this, SystemDashboard.class);
                startActivity(intent);
                finish();


            } else if (id == R.id.navigation_product) {
                AlertDialog alertDialog = new AlertDialog.Builder(CreateUser.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("You are already in the Product Page");
                alertDialog.show();
                // Handle navigation_product action
            } else if (id == R.id.navigation_notifications) {
                AlertDialog alerts = new AlertDialog.Builder(CreateUser.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Notification Page");
                alerts.show();

                // Handle navigation_notifications action
            } else if (id == R.id.create_user) {

                AlertDialog alerts = new AlertDialog.Builder(CreateUser.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Notification Page");
                alerts.show();
                // Handle create_user action

            }
            else if (id == R.id.appinfo) {
                AlertDialog alerts = new AlertDialog.Builder(CreateUser.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Notification Page1");
                alerts.show();

            }
            else if (id == R.id.logoutid) {
                AlertDialog alert = new AlertDialog.Builder(CreateUser.this).create();
                alert.setTitle("Logout");
                alert.setMessage("Are you sure you want to logout?");
                alert.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
                    Intent intent = new Intent(CreateUser.this, MainActivity.class);
                    startActivity(intent);
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