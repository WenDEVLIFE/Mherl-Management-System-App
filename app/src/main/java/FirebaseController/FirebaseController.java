package FirebaseController;

import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mherlsmanagementsystem.BuyProduct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import functions.CreateListener;
import functions.User;
import functions.UserCreationListener;

public class FirebaseController {

    // This used for listener
    private UserCreationListener userCreationListener;

    private CreateListener createListener;

    private BuyProduct buyProduct;


    // This is firebase Singleton
    private static FirebaseController instance;

    private DatabaseReference Database;


    // Singleton pattern
    public static FirebaseController getInstance() {
        if (instance == null) {
            instance = new FirebaseController();
        }
        return instance;
    }

    public FirebaseController(){

        // Our database reference
        Database = FirebaseDatabase.getInstance().getReference();
    }


    public void setUserCreationListener(UserCreationListener userCreationListener) {
        // UserCreationListener is an interface
        this.userCreationListener = userCreationListener;
    }

    public void setCreateListener(CreateListener createListener) {
        // CreateListener is an interface
        this.createListener = createListener;
    }

    public void setBuyListener(BuyProduct buyProduct) {
        // BuyProduct is an interface
        this.buyProduct = buyProduct;

    }

    // This method will create a user
    public void CreateUser(String username1, String password1, String role1, EditText username) {
        DatabaseReference usersRef = Database.child("Users");
        usersRef.orderByChild("username").equalTo(username1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Username already exists
                    // You can handle this case as you see fit, for example, show a message to the user
                    if (userCreationListener != null) {

                        // This is for the failure
                        userCreationListener.onFailure();
                    }
                } else {
                    // Username does not exist, create new user
                    if (userCreationListener != null) {

                        // Id
                        String userId = UUID.randomUUID().toString();

                        // This is for hashmap
                        Map<String, Object> user = new HashMap<>();
                        user.put("username", username1);
                        user.put("password", password1);
                        user.put("role", role1);

                        // then insert the value in hashmap
                        usersRef.child(userId).setValue(user);

                        //This is for adding
                        User user1 = (new User(username1, role1));

                        // This is for the success
                        userCreationListener.onSuccess();

                        LocalDate date = null;
                        LocalTime time = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            // This is for the report
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reports = database.getReference("Reports");
                            String ReportId = UUID.randomUUID().toString();
                            date = LocalDate.now();
                            time = LocalTime.now();
                            String dateformat = date.toString();
                            String timeformat = time.toString();

                            Map <String, Object> report = new HashMap<>();
                            report.put("username", username);
                            report.put("Activity", "User Created");
                            report.put("Date", dateformat);
                            report.put("Time", timeformat);
                            reports.child(ReportId).setValue(report);
                        }



                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
                if (userCreationListener != null) {

                    // This is for the error
                    userCreationListener.onError(databaseError);
                }

            }
        });
    }


    // This method is used to insert create a product
    public void CreateProduct(String productname, int quantityProducts, String priceInput, String username) {

        // Get the product child
        DatabaseReference productsRef = Database.child("Products");

        productsRef.orderByChild("productname").equalTo(productname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Product already exists
                    // You can handle this case as you see fit, for example, show a message to the user
                    if (createListener != null) {

                        // This is for the failure
                        createListener.onFailure();
                    }
                } else {
                    if (createListener != null) {

                        // Product does not exist, create new product
                        // Id

                        String productId = UUID.randomUUID().toString();

                        // This is for hashmap
                        Map<String, Object> product = new HashMap<>();
                        product.put("productname", productname);
                        product.put("quantity", quantityProducts);
                        product.put("price", priceInput);

                        // then insert the value in hashmap
                        productsRef.child(productId).setValue(product);

                        // This is for the success
                        createListener.onSuccess();


                        LocalDate date = null;
                        LocalTime time = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            // This is for the report
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reports = database.getReference("Reports");
                            String ReportId = UUID.randomUUID().toString();
                            date = LocalDate.now();
                            time = LocalTime.now();
                            String dateformat = date.toString();
                            String timeformat = time.toString();

                            Map <String, Object> report = new HashMap<>();
                            report.put("username", username);
                            report.put("Activity", "Create Products");
                            report.put("Date", dateformat);
                            report.put("Time", timeformat);
                            reports.child(ReportId).setValue(report);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
                if (createListener != null) {

                    // This is for the error
                    createListener.onError(databaseError);
                }
            }
        });
    }

    public void BuyProduct(String productname1, int productquantity1, String username) {
        DatabaseReference productsRef = Database.child("Products");

        // get the product name
        productsRef.orderByChild("productname").equalTo(productname1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        // This will get the quantity of the product
                        int quantity = snapshot.child("quantity").getValue(Integer.class);

                        // This will get the price of the product
                        String price = snapshot.child("price").getValue(String.class);
                            if (quantity >= productquantity1) {

                                // This will check if the product is not null
                                if (buyProduct != null) {

                                    // This will update the quantity of the product
                                    int newQuantity = quantity - productquantity1;

                                    // This will update the quantity of the product
                                    snapshot.getRef().child("quantity").setValue(newQuantity);

                                    // This will get the price of the product
                                    int price_value = Integer.parseInt(price);

                                    // This will get the total price of the product
                                    int total_price = price_value * productquantity1;


                                    // This will insert the sales on the database
                                    DatabaseReference salesRef = Database.child("Sales");

                                    // This will create a unique id
                                    String salesId = UUID.randomUUID().toString();

                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                                        // get the localtime
                                        LocalDate date = LocalDate.now();

                                        // convert it to string
                                        String dateformat = date.toString();

                                        // This is for hashmap
                                        Map<String, Object> sale = new HashMap<>();
                                        sale.put("productname", productname1);
                                        sale.put("quantity", productquantity1);
                                        sale.put("date", dateformat);
                                        sale.put("totalprice", total_price);


                                        // then insert the value in hashmap
                                        salesRef.child(salesId).setValue(sale);


                                        // This is for the success
                                        buyProduct.onSuccess();


                                        LocalDate date1 = null;
                                        LocalTime time = null;
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                            // This is for the report
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference reports = database.getReference("Reports");
                                            String ReportId = UUID.randomUUID().toString();
                                            date1 = LocalDate.now();
                                            time = LocalTime.now();
                                            String dateformat1 = date1.toString();
                                            String timeformat = time.toString();

                                            Map <String, Object> report = new HashMap<>();
                                            report.put("username", username);
                                            report.put("Activity", "Buy Products");
                                            report.put("Date", dateformat1);
                                            report.put("Time", timeformat);
                                            reports.child(ReportId).setValue(report);
                                        }
                                    }

                                }


                        } else {
                            // This is for the failure
                            buyProduct.onFailure();

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
                if (buyProduct != null) {

                    // This is for the error
                    buyProduct.onError(databaseError);
                }
            }
        });
    }

    public void Countinfo(TextView admintext, TextView productText, TextView userText, TextView salesText)
    {

        // get the user reference
        DatabaseReference usersRef = Database.child("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    // count the user
                    long count = dataSnapshot.getChildrenCount();
                    userText.setText(String.valueOf(count));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        // get the product reference
        DatabaseReference productsRef = Database.child("Products");
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    // count the product
                    long count = dataSnapshot.getChildrenCount();
                    productText.setText(String.valueOf(count));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        //This will count the sales of the product
        DatabaseReference salesRef = Database.child("Sales");
        salesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    // i want to count the total price

                    // For data snapshot
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        long total_price = 0;
                        int price = snapshot.child("totalprice").getValue(Integer.class);
                        total_price += price;

                        // This will count the price
                        salesText.setText(String.valueOf(total_price));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        // Admin reference
        DatabaseReference adminRef = Database.child("Users");
        adminRef.orderByChild("role").equalTo("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    // count the admin
                    long count = dataSnapshot.getChildrenCount();

                    // This will set the value of the admin
                    admintext.setText(String.valueOf(count));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.


            }
        });
    }
}
