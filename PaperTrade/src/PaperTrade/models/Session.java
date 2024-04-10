package PaperTrade.models;

public class Session {
    private static String email;

    private Session() {}

    public static String getUsername() {
        return email;
    }

    public static void loginUsername(String user) {
        email = user;
    }

    public static void logoutUsername() {
        email = null;
    }

    public boolean isLoggedIn() {
        return email != null;
    }
}
