package FirebaseController;

public class FirebaseController {


    // This is firebase Singleton
    private static FirebaseController instance;

    public static FirebaseController getInstance() {
        if (instance == null) {
            instance = new FirebaseController();
        }
        return instance;
    }



    public void CreateUser(String username1, String password1, String role1) {
        // This is for the add user button


    }
}
