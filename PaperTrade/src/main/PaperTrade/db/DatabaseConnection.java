package main.PaperTrade.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseConnection {
    // JDBC URL, username and password of MySQL server
    private static final String url = "jdbc:mysql://localhost:3306/paper_trade";
    private static final String user = "root";
    private static final String password = "toor";

    // JDBC variables for opening and managing connection
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection to MySQL database failed.");
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    // Method to execute SQL queries like SELECT
    public ResultSet executeQuery(String query) {
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Error executing query: " + query);
            e.printStackTrace();
        }
        return null;
    }

    // Method to execute SQL commands like INSERT, UPDATE, DELETE
    public int executeUpdate(String sql) {
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error executing update: " + sql);
            e.printStackTrace();
        }
        return 0;
    }
}
