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

    private EditText editText, passwordText;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.loginbutton);
        editText = findViewById(R.id.usernameid);
        passwordText = findViewById(R.id.passwordid);
        switch1 = findViewById(R.id.see_password_switch);

        button.setOnClickListener(v -> {
            String enteredUsername = editText.getText().toString().trim();
            String enteredPassword = passwordText.getText().toString().trim();

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // So this will check the value from child in the firebase
            DatabaseReference userRef = database.child("Users");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                // Snaphots are like lines we get from the database
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String usernameFromDB = snapshot.child("username").getValue(String.class);
                        String passwordFromDB = snapshot.child("password").getValue(String.class);
                        if (usernameFromDB != null && usernameFromDB.equals(enteredUsername) && passwordFromDB != null && passwordFromDB.equals(enteredPassword)) {
                            // Username and password match, login successful
                            String userRole = snapshot.child("role").getValue(String.class);
                            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, SystemDashboard.class);
                            intent.putExtra("username", enteredUsername);
                            intent.putExtra("role", userRole);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                    // Username does not exist or password does not match
                    Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Error handling
                    Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
                    Toast.makeText(MainActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                passwordText.setInputType(1);
            } else {
                passwordText.setInputType(129);
            }
        });
    }
}
