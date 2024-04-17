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
                String d = "SELECT * FROM Holds WHERE user_id = " + 
                id + " AND symbol = '" + stock_symbol + "';";
                ResultSet r = DatabaseConnection.getInstance().executeQuery(d);
                if (r.next())
                {
                    DatabaseConnection.getInstance().executeUpdate("UPDATE Holds SET quantity = quantity + " + quantity + 
                        ", avg_price = " + (r.getFloat("avg_price")*r.getInt("quantity") + price*quantity)/(r.getInt("quantity")+quantity)
                        + " WHERE user_id = " + id + " AND symbol = '" + stock_symbol + "';");
                    return true;
                }
                else{
                    DatabaseConnection.getInstance().executeUpdate("INSERT INTO Holds (user_id, symbol, quantity, avg_price) VALUES (" + id + ", '" + stock_symbol + "', " + quantity + ", " + price + ");");
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
    
    public static boolean removeFromHoldings(String stock_symbol, int quantity, float price){
        if(isLoggedIn()){
            try {
                String d = "SELECT * FROM Holds WHERE user_id = " + 
                id + " AND symbol = '" + stock_symbol + "';";
                ResultSet r = DatabaseConnection.getInstance().executeQuery(d);
                if (r.next())
                {
                    DatabaseConnection.getInstance().executeUpdate("UPDATE Holds SET quantity = quantity - " + quantity 
                        + " WHERE user_id = " + id + " AND symbol = '" + stock_symbol + "';");
                    DatabaseConnection.getInstance().executeUpdate("UPDATE User SET balance = balance + " + quantity*price + " WHERE user_id = " + id + ";");
                    return true;
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static float getMarketPrice(String stock_symbol){
        float price = 0;
        ResultSet rs = DatabaseConnection.getInstance().executeQuery(
            "Select close, timestamp, symbol from stock_price where symbol = '" + stock_symbol + 
            "' order by timestamp desc LIMIT 1;"
        );
        try {
            if (rs.next()) {
                price = rs.getFloat("close");
            } else {
                price = 0;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return price;
    }

    public static float get1DayChange(String stock_symbol){
        String s = "SELECT first_entry.Close AS FirstClose, last_entry.Close AS LastClose, (last_entry.Close - first_entry.Close) AS PriceDifference " +
        "FROM (SELECT Close FROM stock_price WHERE Symbol = '"+stock_symbol+"' AND Timestamp = (SELECT MIN(Timestamp) FROM stock_price "+
        "WHERE Symbol = '"+stock_symbol+"' AND DATE(Timestamp) = (SELECT MAX(DATE(Timestamp)) FROM stock_price WHERE Symbol = '"+stock_symbol+"'))) AS first_entry,"+
        "(SELECT Close FROM stock_price WHERE Symbol = '"+stock_symbol+"' AND Timestamp = (SELECT MAX(Timestamp) FROM stock_price "+
        "WHERE Symbol = '"+stock_symbol+"' AND DATE(Timestamp) = (SELECT MAX(DATE(Timestamp)) FROM stock_price WHERE Symbol = '"+stock_symbol+"'))) AS last_entry;";
        ResultSet rs = DatabaseConnection.getInstance().executeQuery(
            s
        );
        // String s = "SELECT first_entry.Close AS FirstClose, last_entry.Close AS LastClose, (last_entry.Close - first_entry.Close) AS PriceDifference " +
        //    "FROM (SELECT Close FROM stock_price WHERE Symbol = '"+stock_symbol+"' AND Timestamp = (SELECT MIN(Timestamp) FROM stock_price "+
        //    "WHERE Symbol = '"+stock_symbol+"' AND DATE(Timestamp) = (SELECT MAX(DATE(Timestamp)) FROM stock_price WHERE Symbol = '"+stock_symbol+"' - INTERVAL 1 DAY))) AS first_entry, "+
        //    "(SELECT Close FROM stock_price WHERE Symbol = '"+stock_symbol+"' AND Timestamp = (SELECT MAX(Timestamp) FROM stock_price "+
        //    "WHERE Symbol = '"+stock_symbol+"' AND DATE(Timestamp) = (SELECT MAX(DATE(Timestamp)) FROM stock_price WHERE Symbol = '"+stock_symbol+"'))) AS last_entry;";
        // ResultSet rs = DatabaseConnection.getInstance().executeQuery(s);

        try {
            if (rs.next()) {
                float priceDifference = rs.getFloat("PriceDifference");
                return priceDifference;
                
            } else {
                return 0;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

}
