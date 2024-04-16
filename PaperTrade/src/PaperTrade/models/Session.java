package PaperTrade.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import PaperTrade.db.DatabaseConnection;

public class Session {
    private static String email;
    private static int id;

    private Session() {}

    public static int getId() {
        return id;
    }

    public static String getUsername() {
        return email;
    }

    public static void loginUsername(String user, int i) {
        email = user;
        id = i;
    }

    public static void logoutUsername() {
        email = null;
        id = 0;
    }

    public static boolean isLoggedIn() {
        return email != null;
    }

    public static boolean addToHoldings(String stock_symbol, int quantity, float price) {
        // Add the stock to the user's holdings
        // Add your code here
        if (isLoggedIn()){
            try {
                ResultSet r = DatabaseConnection.getInstance().executeQuery("SELECT * FROM Holds WHERE user_id = " + 
                    id + " AND stock_symbol = '" + 
                    stock_symbol + "'");
                if (r.next())
                {
                    DatabaseConnection.getInstance().executeUpdate("UPDATE Holds SET quantity = quantity + " + quantity + 
                        ", avg_price = " + (r.getFloat("avg_price")*r.getInt("quantity") + price*quantity)/(r.getInt("quantity")+quantity)
                        + " WHERE user_id = " + id + " AND stock_symbol = '" + stock_symbol + "';");
                    return true;
                }
                else{
                    DatabaseConnection.getInstance().executeUpdate("INSERT INTO Holds (user_id, stock_symbol, quantity, avg_price) VALUES (" + id + ", '" + stock_symbol + "', " + quantity + ", " + price + ");");
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
