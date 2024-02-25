package com.example.mherlsmanagementsystem;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import functions.User;

public class MainActivity extends AppCompatActivity {

    private Button button;

    private Switch switch1;

    private EditText editText , passwordText;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = findViewById(R.id.loginbutton);
        button.setOnClickListener(v -> {

            String enteredUsername = editText.getText().toString();
            String enteredPassword = passwordText.getText().toString();

            database.child("users").child(enteredUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Get user information
                        User user = dataSnapshot.getValue(User.class);

                        if (user.getPassword().equals(enteredPassword)) {
                            // User password matches entered password
                            // Login successful
                            // Navigate to next activity here


                            String userRole = user.getRole();
                            // Write a message to the database
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("message");

                            myRef.setValue("Hello, World!");

                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Welcome to MHERLS Management System");
                            alertDialog.show();

                            Intent intent = new Intent(MainActivity.this, SystemDashboard.class);
                            startActivity(intent);

                            intent.putExtra("username", enteredUsername);
                            intent.putExtra("role", userRole);
                            finish();

                        } else {
                            // User password does not match entered password
                            // Login failed
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // User does not exist
                        Toast.makeText(MainActivity.this, "User does not exist.", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting user failed
                    Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
                }
            });

        });

        editText = findViewById(R.id.usernameid);
        passwordText = findViewById(R.id.passwordid);


        switch1 = findViewById(R.id.see_password_switch);
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // The switch is enabled/checked
                passwordText.setInputType(1);

            } else {
                // The switch is disabled
                passwordText.setInputType(129);
            }
        });

    }
}