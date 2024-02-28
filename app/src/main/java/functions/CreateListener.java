package functions;

import com.google.firebase.database.DatabaseError;

public interface CreateListener {

    // Access the void using interface
    void onSuccess();
    void onFailure();
    void onError(DatabaseError databaseError);

}
