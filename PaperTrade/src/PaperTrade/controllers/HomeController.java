package PaperTrade.controllers;

import java.sql.ResultSet;

import PaperTrade.Main;
import PaperTrade.db.DatabaseConnection;
import PaperTrade.models.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class HomeController {
    @FXML
    private HBox navbar;
    @FXML 
    private HBox top_gainers;
    @FXML 
    private HBox top_losers;
    @FXML
    private VBox all_stocks;
    @FXML 
    private ListView<String> watchlist;
    @FXML
    private Label tutorial;

    public void initialize() {
        // Initialize the controller
        // Add any initialization logic here
        Main.getInstance().setupNavbar(navbar);
        addStocks();
        addWatchlist();
        tutorial.setOnMouseClicked(e -> {
            try {
                Main.getInstance().goToTutorial();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void addWatchlist(){
        try {
            ResultSet rs = DatabaseConnection.getInstance().executeQuery("SELECT List_Name FROM Watchlist WHERE User_ID = " + Session.getId() + ";");
            while(rs.next()){
                String name = rs.getString("List_Name");
                watchlist.getItems().add(name);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        
    }

    @FXML 
    private void populateAllStocks(){
        // Populate all stocks
        // Add any logic here
        String query = "SELECT first_entry.Timestamp AS FirstTimestamp, first_entry.Close AS FirstClose, last_entry.Timestamp AS LastTimestamp, last_entry.Close AS LastClose, s.Symbol as symbol, s.Name as name, " +
        "(last_entry.Close - first_entry.Close)/first_entry.close*100 AS profit " +
        "FROM stock s " +
        "JOIN (SELECT Symbol, Timestamp, Close FROM stock_price WHERE (Symbol, Timestamp) IN (SELECT Symbol, MAX(Timestamp) AS Timestamp FROM stock_price WHERE DATE(Timestamp) = (SELECT Date(MAX(Timestamp) - INTERVAL 1 DAY) FROM stock_price) GROUP BY Symbol)) AS first_entry ON s.Symbol = first_entry.Symbol " +
        "JOIN (SELECT Symbol, Timestamp, Close FROM stock_price WHERE (Symbol, Timestamp) IN (SELECT Symbol, MAX(Timestamp) AS Timestamp FROM stock_price GROUP BY Symbol)) AS last_entry ON s.Symbol = last_entry.Symbol";

        try {
            ResultSet rs = DatabaseConnection.getInstance().executeQuery(query);
            while(rs.next()){
                String symbol = rs.getString("symbol");
                String name = rs.getString("name");

                float price = rs.getFloat("LastClose");
                float profit = rs.getFloat("profit");
                all_stocks.getChildren().add(getVBoxStocks(symbol, name, price, profit));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void showTopGainers(){
        String query = "SELECT first_entry.Timestamp AS FirstTimestamp, first_entry.Close AS FirstClose, last_entry.Timestamp AS LastTimestamp, last_entry.Close AS LastClose, s.Symbol as symbol, s.Name as name, " +
        "(last_entry.Close - first_entry.Close)/first_entry.close*100 AS profit " +
        "FROM stock s " +
        "JOIN (SELECT Symbol, Timestamp, Close FROM stock_price WHERE (Symbol, Timestamp) IN (SELECT Symbol, MAX(Timestamp) AS Timestamp FROM stock_price WHERE DATE(Timestamp) = (SELECT Date(MAX(Timestamp) - INTERVAL 1 DAY) FROM stock_price) GROUP BY Symbol)) AS first_entry ON s.Symbol = first_entry.Symbol " +
        "JOIN (SELECT Symbol, Timestamp, Close FROM stock_price WHERE (Symbol, Timestamp) IN (SELECT Symbol, MAX(Timestamp) AS Timestamp FROM stock_price GROUP BY Symbol)) AS last_entry ON s.Symbol = last_entry.Symbol ORDER BY profit DESC LIMIT 4";

        try {
            ResultSet rs = DatabaseConnection.getInstance().executeQuery(query);
            while(rs.next()){
                String symbol = rs.getString("symbol");
                String name = rs.getString("name");

                float price = rs.getFloat("LastClose");
                float profit = rs.getFloat("profit");
                top_gainers.getChildren().add(getHBoxStocks(symbol, name, price, profit));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }@FXML
    private void showTopLosers(){
        String query = "SELECT first_entry.Timestamp AS FirstTimestamp, first_entry.Close AS FirstClose, last_entry.Timestamp AS LastTimestamp, last_entry.Close AS LastClose, s.Symbol as symbol, s.Name as name, " +
        "(last_entry.Close - first_entry.Close)/first_entry.close*100 AS profit " +
        "FROM stock s " +
        "JOIN (SELECT Symbol, Timestamp, Close FROM stock_price WHERE (Symbol, Timestamp) IN (SELECT Symbol, MAX(Timestamp) AS Timestamp FROM stock_price WHERE DATE(Timestamp) = (SELECT Date(MAX(Timestamp) - INTERVAL 1 DAY) FROM stock_price) GROUP BY Symbol)) AS first_entry ON s.Symbol = first_entry.Symbol " +
        "JOIN (SELECT Symbol, Timestamp, Close FROM stock_price WHERE (Symbol, Timestamp) IN (SELECT Symbol, MAX(Timestamp) AS Timestamp FROM stock_price GROUP BY Symbol)) AS last_entry ON s.Symbol = last_entry.Symbol ORDER BY profit ASC LIMIT 4";

        try {
            ResultSet rs = DatabaseConnection.getInstance().executeQuery(query);
            while(rs.next()){
                String symbol = rs.getString("symbol");
                String name = rs.getString("name");

                float price = rs.getFloat("LastClose");
                float profit = rs.getFloat("profit");
                top_losers.getChildren().add(getHBoxStocks(symbol, name, price, profit));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void addStocks(){
        // Add stocks to the home page

        showTopGainers();
        showTopLosers();
        populateAllStocks();
    }

    private HBox getVBoxStocks(String symbol, String name, float price, float profit){
        HBox stock = new HBox();
        stock.setSpacing(10);
        stock.setAlignment(javafx.geometry.Pos.CENTER);
        stock.getStyleClass().add("stock_vbox");
        Label symboLabel = new Label(symbol);
        symboLabel.setFont(new Font("Arial", 12));
        Label nameLabel = new Label(name);
        nameLabel.setFont(new Font("Arial", 16));
        Label priceLabel = new Label("\u20B9" + price);
        priceLabel.setFont(new Font("Arial", 14));
        Label profitLabel = new Label( String.format("%.2f",profit) + "%");
        if(profit > 0){
            profitLabel.setStyle("-fx-text-fill: green;");
        }else{
            profitLabel.setStyle("-fx-text-fill: red;");
        }
        Pane pane = new Pane();
        HBox.setHgrow(pane, Priority.ALWAYS);
        stock.getChildren().addAll(nameLabel, symboLabel, pane, priceLabel, profitLabel);

        stock.setOnMouseClicked(e -> {
            try {
                Main.getInstance().openStock(symbol);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return stock;
    }

    private VBox getHBoxStocks(String symbol, String name, float price, float profit){
        VBox stock = new VBox();
        stock.getStyleClass().add("stock_hbox");
        stock.setPrefWidth(170);
        stock.setPrefHeight(100);
        Label symboLabel = new Label(symbol);
        symboLabel.setFont(new Font("Arial", 12));
        Label nameLabel = new Label(name);
        nameLabel.setFont(new Font("Arial", 16));
        Label priceLabel = new Label("\u20B9" + price);
        priceLabel.setFont(new Font("Arial", 14));
        Label profitLabel = new Label( String.format("%.2f",profit)  + "%");
        if(profit > 0){
            profitLabel.setStyle("-fx-text-fill: green;");
        }else{
            profitLabel.setStyle("-fx-text-fill: red;");
        }
        Pane pane = new Pane();
        VBox.setVgrow(pane, Priority.ALWAYS);
        stock.getChildren().addAll(nameLabel, symboLabel, pane, priceLabel, profitLabel);


        return stock;
    }

}

