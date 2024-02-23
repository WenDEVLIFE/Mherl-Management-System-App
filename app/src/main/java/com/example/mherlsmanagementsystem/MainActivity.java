package com.example.mherlsmanagementsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Button button;

    private Switch switch1;

    private EditText editText , passwordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = findViewById(R.id.loginbutton);
        button.setOnClickListener(v -> {
            // Do something in response to button click

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
            finish();

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