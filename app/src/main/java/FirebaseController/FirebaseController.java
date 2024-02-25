package FirebaseController;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import functions.User;
import functions.UserCreationListener;

public class FirebaseController {

    // This used for listener
    private UserCreationListener userCreationListener;

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
}
