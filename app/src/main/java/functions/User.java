package functions;

public class User {


    private String username;

    private String role;

    public User(String username, String role) {
        this.username = username;

        this.role = role;
    }

    public String getUsername() {
        return username;
    }



    public String getRole() {
        return role;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void setRole(String role) {
        this.role = role;
    }

    public String toString() {
        return "Username: " + username  + ", Role: " + role;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User user = (User) obj;
        return user.username.equals(username) && user.role.equals(role);
    }

    public String Username() {
        return username;

    }

    public String Role() {
        return role;
    }
}
