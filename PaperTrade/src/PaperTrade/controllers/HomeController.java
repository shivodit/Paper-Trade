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

    public void initialize() {
        // Initialize the controller
        // Add any initialization logic here
        Main.getInstance().setupNavbar(navbar);
        addStocks();
    }

    @FXML 
    private void populateAllStocks(){
        // Populate all stocks
        // Add any logic here
        try {
            ResultSet rs = DatabaseConnection.getInstance().executeQuery("SELECT * FROM stock;");
            while(rs.next()){
                String symbol = rs.getString("symbol");
                String name = rs.getString("name");

                float price = Session.getMarketPrice(symbol);
                float profit = Session.get1DayChange(symbol);
                all_stocks.getChildren().add(getVBoxStocks(symbol, name, price, profit));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void addStocks(){
        // Add stocks to the home page
        // Add any logic here
        top_gainers.getChildren().add(getHBoxStocks("AAPL", "Apple Inc.", 123.45f, 2.5f));
        top_gainers.getChildren().add(getHBoxStocks("GOOGL", "Alphabet Inc.", 1234.56f, 1.5f));
        top_gainers.getChildren().add(getHBoxStocks("MSFT", "Microsoft Corporation", 234.56f, 3.5f));
        top_gainers.getChildren().add(getHBoxStocks("AMZN", "Amazon.com Inc.", 3456.78f, 4.5f));

        top_losers.getChildren().add(getHBoxStocks("TSLA", "Tesla Inc.", 456.78f, -2.5f));
        top_losers.getChildren().add(getHBoxStocks("FB", "Facebook Inc.", 234.56f, -1.5f));
        top_losers.getChildren().add(getHBoxStocks("NFLX", "Netflix Inc.", 345.67f, -3.5f));
        top_losers.getChildren().add(getHBoxStocks("NVDA", "NVIDIA Corporation", 456.78f, -4.5f));

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
        Label profitLabel = new Label( profit + "%");
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
        Label profitLabel = new Label( profit + "%");
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

