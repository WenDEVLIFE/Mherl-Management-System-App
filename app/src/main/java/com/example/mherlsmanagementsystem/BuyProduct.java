package com.example.mherlsmanagementsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;

import FirebaseController.FirebaseController;
import functions.BuyListener;

public class BuyProduct extends AppCompatActivity implements BuyListener {
    TextView usernametext, RoleText;

    String username, role, Productname;

    EditText productname, productquantity;

    Button buy, back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        role = intent.getStringExtra("role");
        Productname = intent.getStringExtra("productname");

        productname = findViewById(R.id.productname);
        productname.setText(Productname);
        productname.setEnabled(false);
        productquantity = findViewById(R.id.quantity);

       buy = findViewById(R.id.buttonadd);
       buy.setOnClickListener(v -> {
           // This will add the product to the database

           String productname1 = productname.getText().toString();
           int productquantity1 = Integer.parseInt(productquantity.getText().toString());

           if (productquantity1 <= 0) {
               AlertDialog alertDialog = new AlertDialog.Builder(BuyProduct.this).create();
               alertDialog.setTitle("Alert");
               alertDialog.setMessage("Quantity should be greater than 0");
               alertDialog.show();
           } else {

                FirebaseController buyproduct = FirebaseController.getInstance();
                buyproduct.setBuyListener(BuyProduct.this);
                buyproduct.BuyProduct(productname1, productquantity1);

           }



        });


       back = findViewById(R.id.buttonback);
       back.setOnClickListener(v -> {
                    // This will go back to the product page
                    Intent intent1 = new Intent(BuyProduct.this, Product.class);
                    intent1.putExtra("username", username);
                    intent1.putExtra("role", role);
                    startActivity(intent1);
                    finish();
                });


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
                AlertDialog alertDialog = new AlertDialog.Builder(BuyProduct.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("You are already in the Home Page");
                alertDialog.show();


            } else if (id == R.id.navigation_product) {
                Intent intent1 = new Intent(BuyProduct.this, Product.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role", role);
                startActivity(intent1);
                // Handle navigation_product action
            } else if (id == R.id.navigation_notifications) {

                // alerts
                AlertDialog alerts = new AlertDialog.Builder(BuyProduct.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Notification Page");
                alerts.show();

                // Handle navigation_notifications action
            } else if (id == R.id.create_user) {

                // This will go to add user
                AlertDialog alerts = new AlertDialog.Builder(BuyProduct.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Notification Page");
                alerts.show();
                // Handle create_user action
                Intent intent1 = new Intent(BuyProduct.this, AddUser.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role", role);
                startActivity(intent1);
                finish();
            }
            else if (id == R.id.appinfo) {

                AlertDialog alerts = new AlertDialog.Builder(BuyProduct.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Notification Page1");
                alerts.show();

            }
            else if (id == R.id.logoutid) {

                // This is logout action
                AlertDialog alert = new AlertDialog.Builder(BuyProduct.this).create();
                alert.setTitle("Logout");
                alert.setMessage("Are you sure you want to logout?");
                alert.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
                    Intent intent2 = new Intent(BuyProduct.this, MainActivity.class);
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
            AlertDialog dialog = new AlertDialog.Builder(BuyProduct.this).create();
            dialog.setTitle("Created a product");
            dialog.setMessage("You successfully created a product");
            dialog.show();


            // This will set default value of the fields

            productquantity.setText("");


        });
    }

    @Override
    public void onFailure() {
        runOnUiThread(() -> {

            // This will show the alert dialog
            AlertDialog alertDialog = new AlertDialog.Builder(BuyProduct.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Product already exists");
            alertDialog.show();
        });

    }

    @Override
    public void onError(DatabaseError databaseError) {
        runOnUiThread(() -> {

            // This will show the alert dialog
            AlertDialog alertDialog = new AlertDialog.Builder(BuyProduct.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Error: " + databaseError.getMessage());
            alertDialog.show();
        });

    }


}