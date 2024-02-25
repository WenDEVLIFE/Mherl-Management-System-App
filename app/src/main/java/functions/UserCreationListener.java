package functions;

import com.google.firebase.database.DatabaseError;

public interface UserCreationListener {
    void onSuccess();
    void onFailure();
    void onError(DatabaseError databaseError);
}
