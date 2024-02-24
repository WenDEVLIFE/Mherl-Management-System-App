package FirebaseController;

public class FirebaseController {

    private static FirebaseController instance;

    private static FirebaseController getInstance() {
        if (instance == null) {
            instance = new FirebaseController();
        }
        return instance;
    }


    public void CreateUser() {
        // This is for the add user button

    }
}
