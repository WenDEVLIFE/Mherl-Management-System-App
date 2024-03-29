package com.example.mherlsmanagementsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalTime;
import java.util.Objects;

import FirebaseController.FirebaseController;

public class SystemDashboard extends AppCompatActivity {

    TextView usernametext, RoleText, greetings;

    String username, role;

    TextView Admintext, ProductText, UserText, SalesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_dashboard);

        // This is for the background image
        ImageView imageView = findViewById(R.id.sunrisegif);

        // we use this to get the current time
        LocalTime now = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            now = LocalTime.now();
        }

        LocalTime morningStart = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            morningStart = LocalTime.of(6, 0);
        }
        LocalTime morningEnd = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            morningEnd = LocalTime.of(11, 59);
        }

        LocalTime noonStart = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            noonStart = LocalTime.of(12, 0);
        }
        LocalTime noonEnd = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            noonEnd = LocalTime.of(16, 59);
        }

        LocalTime eveningStart = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            eveningStart = LocalTime.of(17, 0);
        }
        LocalTime eveningEnd = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            eveningEnd = LocalTime.of(20, 59);
        }

        LocalTime nightStart = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            nightStart = LocalTime.of(21, 0);
        }

        LocalTime nightEnd = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            nightEnd = LocalTime.of(5, 59);
        }

        // Then to check the localtime and set the background image
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if ((now.isAfter(morningStart) && now.isBefore(morningEnd)) || now.equals(morningStart) || now.equals(morningEnd)) {
                System.out.println("It's morning");
                Glide.with(this).load(R.drawable.sun).into(imageView);
            } else if ((now.isAfter(noonStart) && now.isBefore(noonEnd)) || now.equals(noonStart) || now.equals(noonEnd)) {
                System.out.println("It's noon");
                Glide.with(this).load(R.drawable.fieldunscreen).into(imageView);
            } else if ((now.isAfter(eveningStart) && now.isBefore(eveningEnd)) || now.equals(eveningStart) || now.equals(eveningEnd)) {
                System.out.println("It's evening");
                Glide.with(this).load(R.drawable.night).into(imageView);
            } else if ((now.isAfter(nightStart) && now.isBefore(nightEnd)) || now.equals(nightStart) || now.equals(nightEnd)) {
                System.out.println("It's night");
                Glide.with(this).load(R.drawable.night).into(imageView);
            }
        }


        // We will get the intent from other tabs
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        role = intent.getStringExtra("role");

        // To greet the user
        greetings = findViewById(R.id.textView);
        greetings.setText("Welcome "+ username);

        // This is for the count of the admin, product, user and sales
        Admintext = findViewById(R.id.admintext);
        ProductText = findViewById(R.id.producttext);
        UserText = findViewById(R.id.usertext);
        SalesText = findViewById(R.id.salestext);

        // Send the count to the firebase
         FirebaseController count = FirebaseController.getInstance();
         count.Countinfo(Admintext, ProductText, UserText, SalesText);

         // This is for the navigation view
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
                AlertDialog alertDialog = new AlertDialog.Builder(SystemDashboard.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("You are already in the Home Page");
                alertDialog.show();

            }

            // This will go to products
            else if (id == R.id.navigation_product) {

                // This will go to products
                Intent intent1 = new Intent(SystemDashboard.this, Product.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role", role);
                startActivity(intent1);
                finish();
                // Handle navigation_product action
            } else if (id == R.id.navigation_notifications) {

                // Go to sales
                Intent intent1 = new Intent(SystemDashboard.this, SalesProducts.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role", role);
                startActivity(intent1);
                finish();


                // Handle navigation_notifications action
            } else if (id == R.id.create_user) {
                if (role != null && role.equals("Admin")){
                    // This will go to add user
                    // Handle create_user action
                    Intent intent1 = new Intent(SystemDashboard.this, AddUser.class);
                    intent1.putExtra("username", username);
                    intent1.putExtra("role", role);
                    startActivity(intent1);
                    finish();
                } else{
                    AlertDialog alerts = new AlertDialog.Builder(SystemDashboard.this).create();
                    alerts.setTitle("Alert");
                    alerts.setMessage("You are not an Admin, you can't access this page");
                    alerts.show();

                }
            }
            else if (id == R.id.appinfo) {

                // This will go to app info
                Intent intent1 = new Intent(SystemDashboard.this, AppInfo.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role", role);
                startActivity(intent1);
                finish();


            }
            else if (id == R.id.logoutid) {

                // This is logout action
                AlertDialog alert = new AlertDialog.Builder(SystemDashboard.this).create();
                alert.setTitle("Logout");
                alert.setMessage("Are you sure you want to logout?");
                alert.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
                    Intent intent2 = new Intent(SystemDashboard.this, MainActivity.class);
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
            Intent intent = new Intent(SystemDashboard.this, MainActivity.class);
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