package com.example.mherlsmanagementsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;

import java.util.List;

import FirebaseController.FirebaseController;
import functions.CreateListener;
import functions.ProductAdapter;
import functions.ProductBase;
import functions.User;
import functions.UserAdapter;

public class CreateProducts extends AppCompatActivity implements CreateListener {


    TextView usernametext, RoleText;

    EditText product, price, quantity;

    Button add, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_products);

        // Get the intent from the previous activity
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String role = intent.getStringExtra("role");

        // Id of the fields
         product = findViewById(R.id.productname);
         quantity = findViewById(R.id.quantity);
         price = findViewById(R.id.Pricefield);

         // Add products button
        add = findViewById(R.id.buttonadd);
        add.setOnClickListener(v -> {
            // This is for adding products
            String productname = product.getText().toString();
            int quantity_products = Integer.parseInt(quantity.getText().toString());
            String price_input = price.getText().toString();

            if(productname.isEmpty() || price_input.isEmpty()){
                AlertDialog alertDialog = new AlertDialog.Builder(CreateProducts.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Please fill all the fields or input a value");
                alertDialog.show();
                return;
            } else{
               if (quantity_products>=1){
                   FirebaseController firebaseController = FirebaseController.getInstance();

                   // Add the createlistener
                   firebaseController.setCreateListener(this);

                   // Then send it to the parameters
                   firebaseController.CreateProduct(productname, quantity_products, price_input, username);
               } else{
                     AlertDialog alertDialog = new AlertDialog.Builder(CreateProducts.this).create();
                     alertDialog.setTitle("Alert");
                     alertDialog.setMessage("Please input a value greater than 0");
                     alertDialog.show();
               }
            }

        });


        // This is back function to go back into the Create Product page huhuh 1:00 am
        back = findViewById(R.id.buttonback);
        back.setOnClickListener(v -> {
            // This is for going back to the product page
            Intent intent1 = new Intent(CreateProducts.this, Product.class);
            intent1.putExtra("username", username);
            intent1.putExtra("role", role);
            startActivity(intent1);
            finish();
        });



        // Our navagation view
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
                // Handle navigation_home action
                Intent intent1 = new Intent(CreateProducts.this, SystemDashboard.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role",role);
                startActivity(intent1);
                finish();


            } else if (id == R.id.navigation_product) {

                // Go to products
                // Handle navigation_product action
                AlertDialog alerts = new AlertDialog.Builder(CreateProducts.this).create();
                alerts.setTitle("Alert");
                alerts.setMessage("You are already in the Product page");
                alerts.show();

            } else if (id == R.id.navigation_notifications) {

                // Go to sales
                Intent intent1 = new Intent(CreateProducts.this, SalesProducts.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role", role);
                startActivity(intent1);
                finish();


                // Handle navigation_notifications action
            } else if (id == R.id.create_user) {
                if (role != null && role.equals("Admin")) {
                    // This will go to add user
                    // Handle create_user action
                    Intent intent1 = new Intent(CreateProducts.this, AddUser.class);
                    intent1.putExtra("username", username);
                    intent1.putExtra("role", role);
                    startActivity(intent1);
                    finish();
                }
                else {
                    AlertDialog alerts = new AlertDialog.Builder(CreateProducts.this).create();
                    alerts.setTitle("Alert");
                    alerts.setMessage("You are not an Admin, you can't access this page");
                    alerts.show();
                }
            }
            else if (id == R.id.appinfo) {

                // This will go to app info
                Intent intent1 = new Intent(CreateProducts.this, AppInfo.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role", role);
                startActivity(intent1);
                finish();


            }
            else if (id == R.id.logoutid) {

                // This is logout action
                AlertDialog alert = new AlertDialog.Builder(CreateProducts.this).create();
                alert.setTitle("Logout");
                alert.setMessage("Are you sure you want to logout?");
                alert.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
                    Intent intent2 = new Intent(CreateProducts.this, MainActivity.class);
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
            AlertDialog dialog = new AlertDialog.Builder(CreateProducts.this).create();
            dialog.setTitle("Created a product");
            dialog.setMessage("You successfully created a product");
            dialog.show();


            // This will set default value of the fields

            product.setText("");
            price.setText("");
            quantity.setText("");

        });
    }

    @Override
    public void onFailure() {
        runOnUiThread(() -> {

            // This will show the alert dialog
            AlertDialog alertDialog = new AlertDialog.Builder(CreateProducts.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Product already exists");
            alertDialog.show();
        });

    }

    @Override
    public void onError(DatabaseError databaseError) {
        runOnUiThread(() -> {

            // This will show the alert dialog
            AlertDialog alertDialog = new AlertDialog.Builder(CreateProducts.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Error: " + databaseError.getMessage());
            alertDialog.show();
        });

    }

}