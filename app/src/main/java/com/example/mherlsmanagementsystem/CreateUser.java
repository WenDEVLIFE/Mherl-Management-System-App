package com.example.mherlsmanagementsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Observable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import FirebaseController.FirebaseController;
import functions.UserCreationListener;

public class CreateUser extends AppCompatActivity implements UserCreationListener {


    TextView usernametext, RoleText;

     Button adduser, back;

     EditText username, password;

    Spinner role;

    CheckBox seePassword;

    String username1, role1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        // We will get the intent from other tabs
        Intent intent = getIntent();
        username1 = intent.getStringExtra("username");
        role1 = intent.getStringExtra("role");

        // UsernameField and PasswordField
        username = findViewById(R.id.usernamefield);
        password = findViewById(R.id.passwordfield);


        // This is for the checkbox
        seePassword = findViewById(R.id.checkBox);
        seePassword.setOnClickListener(v -> {
            if (seePassword.isChecked()) {
                password.setInputType(1);
            } else {
                password.setInputType(129);
            }
        });


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
            String username1 = username.getText().toString();
            String password1 = password.getText().toString();
            String role1 = role.getSelectedItem().toString();

            if(username1.isEmpty() || password1.isEmpty() || role1.equals("Select Role")){
                AlertDialog alertDialog = new AlertDialog.Builder(CreateUser.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Please fill all the fields");
                alertDialog.show();
                return;
            } else {

                // This will check ifi t has a length of 8 or greater
                if(password1.length()>=8){
                    boolean hasSpecial = hasSpecialCharacters(password1);
                    boolean hasUppercase = HasUpperCase(password1);

                    // This will check if it has a special character
                    if(hasSpecial && hasUppercase){

                        // send to the firebase controller
                        FirebaseController send = FirebaseController.getInstance();
                        send.setUserCreationListener(this);
                        send.CreateUser(username1, password1, role1);


                    } else{
                        Toast.makeText(CreateUser.this, "Password must contain at least one special character or an Uppercase", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(CreateUser.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                }



            }
        });


        // This is for the back button
        back = findViewById(R.id.buttonback);
        back.setOnClickListener(v -> {
            Intent intent1 = new Intent(CreateUser.this, AddUser.class);
            intent1.putExtra("username", username1);
            intent1.putExtra("role", role1);
            startActivity(intent1);
            finish();
        });

        // This is navigation bar
        NavigationView navigationView = findViewById(R.id.nav_view);
        usernametext = navigationView.getHeaderView(0).findViewById(R.id.username);
        RoleText = navigationView.getHeaderView(0).findViewById(R.id.Role);
        usernametext.setText("Username:" + username1);
        RoleText.setText("Role:" + role1);

        // event listener of the navigation view
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {

                // Handle navigation_home action
                Intent intent1 = new Intent(CreateUser.this, SystemDashboard.class);
                intent1.putExtra("username", username1);
                intent1.putExtra("role", role1);
                startActivity(intent1);
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

                // Handle create_user action
                Intent intent1 = new Intent(CreateUser.this, AddUser.class);
                startActivity(intent1);
                intent1.putExtra("username", username1);
                intent1.putExtra("role", role1);
                finish();
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
                    Intent intent2 = new Intent(CreateUser.this, MainActivity.class);
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
    @Override
    public void onSuccess() {
        runOnUiThread(() -> {

            // This will show the alert dialog
            AlertDialog dialog = new AlertDialog.Builder(CreateUser.this).create();
            dialog.setTitle("Created a user");
            dialog.setMessage("You successfully created a user");
            dialog.show();


            // This will set default value of the fields
            username.setText("");
            password.setText("");
            role.setSelection(0);
        });
    }

    @Override
    public void onFailure() {
        runOnUiThread(() -> {

            // This will show the alert dialog
            AlertDialog alertDialog = new AlertDialog.Builder(CreateUser.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Username already exists");
            alertDialog.show();
        });

    }

    @Override
    public void onError(DatabaseError databaseError) {
        runOnUiThread(() -> {

            // This will show the alert dialog
            AlertDialog alertDialog = new AlertDialog.Builder(CreateUser.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Error: " + databaseError.getMessage());
            alertDialog.show();
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
            Intent intent = new Intent(CreateUser.this, MainActivity.class);
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

    // This will check if it has a special character
    public boolean hasSpecialCharacters(String password1) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(password1);
        return matcher.find();
    }

    public boolean HasUpperCase(String password1){
        Pattern uppercasePattern = Pattern.compile("[A-Z]");
        Matcher uppercaseMatcher = uppercasePattern.matcher(password1);
        boolean hasUppercase = uppercaseMatcher.find();

        return  hasUppercase;
    }

}