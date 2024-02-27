package FirebaseController;

import androidx.annotation.NonNull;

import com.example.mherlsmanagementsystem.BuyProduct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        this.buyProduct = buyProduct;

    }

    // This method will create a user
    public void CreateUser(String username1, String password1, String role1) {
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
    public void CreateProduct(String productname, int quantityProducts, String priceInput) {

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

    public void BuyProduct(String productname1, int productquantity1) {
        DatabaseReference productsRef = Database.child("Products");

        // get the product name
        productsRef.orderByChild("productname").equalTo(productname1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        int quantity = snapshot.child("quantity").getValue(Integer.class);
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

                                    // This is for hashmap
                                    Map<String, Object> sale = new HashMap<>();
                                    sale.put("productname", productname1);
                                    sale.put("quantity", productquantity1);
                                    sale.put("price", total_price);

                                    // then insert the value in hashmap
                                    salesRef.child(salesId).setValue(sale);


                                    // This is for the success
                                    buyProduct.onSuccess();
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

}
