package PaperTrade.controllers;


import java.sql.ResultSet;
import java.sql.SQLException;

import PaperTrade.Main;
import PaperTrade.db.DatabaseConnection;
import PaperTrade.models.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class StockController {  
    @FXML
    private Label home_button;
    @FXML
    private Label investments_button;
    @FXML
    private TextField search_textfield;
    @FXML
    private Button search_button;
    @FXML
    private MenuButton profile_dropdown;
    @FXML
    private MenuItem profile_button;
    @FXML
    private MenuItem orders_button;
    @FXML
    private Label stock_name_label;
    @FXML
    private Label symbol_label;
    @FXML
    private Label price_label;
    @FXML
    private Label stock_name_label_buy;
    @FXML
    private Label profit_label;
    @FXML
    private LineChart<String, Number> stock_chart;
    @FXML
    private HBox graph_buttons;
    @FXML
    private Label stock_buy_price;
    @FXML
    private HBox navbar;
    @FXML
    private TextField price_textfield;
    @FXML
    private TextField quantity_textfield;
    @FXML
    private Button price_type_toggle;
    @FXML 
    private Button execute_order_button;
    @FXML 
    private Label required_label;
    @FXML
    private Label balance_label;

    private float required_balance = 0f;
    private float price = 0f;
    private String stock_symbol;
    private String stock_name;
    private float offered_price = 0f;
    private int quantity = 0;
    

    public StockController(String stock_symbol) {
        this.stock_symbol = stock_symbol;
        try {
            ResultSet rs = DatabaseConnection.getInstance().executeQuery(
                "SELECT name FROM stock WHERE symbol = '" + stock_symbol + "'"
            );
            if (rs.next()) {
                this.stock_name = rs.getString("name");
            } else {
                this.stock_name = "Stock not found";
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            this.stock_name = "Error fetching stock name";
        }
    }

    public void initialize() {
        // Initialize the controller
        // Add any initialization logic here
        stock_name_label.setText(stock_name);
        stock_name_label_buy.setText(stock_name);
        symbol_label.setText(stock_symbol);
        
        // Fetch the price of the stock from the database
        // TODO live price fetching
        try {
            ResultSet rs = DatabaseConnection.getInstance().executeQuery(
                "Select close, timestamp, symbol from stock_price where symbol = '" + stock_symbol + 
                "' order by timestamp desc LIMIT 1;"
            );
            if (rs.next()) {
                price = rs.getFloat("close");
            } else {
                price = 0;
            }
            rs = DatabaseConnection.getInstance().executeQuery(
                "SELECT first_entry.Close AS FirstClose, last_entry.Close AS LastClose, (last_entry.Close - first_entry.Close) AS PriceDifference " +
                "FROM (SELECT Close FROM stock_price WHERE Symbol = '"+stock_symbol+"' AND Timestamp = (SELECT MIN(Timestamp) FROM stock_price "+
                "WHERE Symbol = '"+stock_symbol+"' AND DATE(Timestamp) = (SELECT MAX(DATE(Timestamp)) FROM stock_price WHERE Symbol = '"+stock_symbol+"'))) AS first_entry,"+
                "(SELECT Close FROM stock_price WHERE Symbol = '"+stock_symbol+"' AND Timestamp = (SELECT MAX(Timestamp) FROM stock_price "+
                "WHERE Symbol = '"+stock_symbol+"' AND DATE(Timestamp) = (SELECT MAX(DATE(Timestamp)) FROM stock_price WHERE Symbol = '"+stock_symbol+"'))) AS last_entry;"

            );
            if (rs.next()) {
                float priceDifference = rs.getFloat("PriceDifference");
                if (priceDifference > 0) {
                    profit_label.setText("+₹" + Math.abs(priceDifference)+" ("+Math.round(priceDifference/price*10000)/100f+"%)");
                    profit_label.setStyle("-fx-text-fill: #00cc00;");
                } else {
                    profit_label.setText("-₹" + Math.abs(priceDifference)+" ("+Math.round(priceDifference/price*10000)/100f+"%)");
                    profit_label.setStyle("-fx-text-fill: red;");
                }
            } else {
                profit_label.setText("profit/loss not available");
            }
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            price = 0;
        }
        price_label.setText("₹ "+price);
        stock_buy_price.setText("₹ "+price);

        // add the graph buttons
        Button one_day = new Button("1D");
        one_day.setOnAction(e -> showTable(1));
        Button five_days = new Button("5D");
        five_days.setOnAction(e -> showTable(5));
        Button one_month = new Button("1M");
        one_month.setOnAction(e -> showTable(30));
        Button three_months = new Button("3M");
        three_months.setOnAction(e -> showTable(90));
        Button six_months = new Button("6M");
        six_months.setOnAction(e -> showTable(180));
        Button one_year = new Button("1Y");
        one_year.setOnAction(e -> showTable(365));
        Button five_years = new Button("5Y");
        five_years.setOnAction(e -> showTable(1825));
        graph_buttons.getChildren().addAll(one_day, five_days, one_month, three_months, six_months, one_year, five_years);
        
        // draw initial chart
        drawChart(stock_chart, 40);
        
        // setup market price toggle
        toggleMarketPrice();

        // setup available balance
        try {
            ResultSet rs = DatabaseConnection.getInstance().executeQuery(
                "SELECT balance FROM user WHERE email = '" + Session.getUsername() + "';"
            );
            if (rs.next()) {
                balance_label.setText("Balance available : ₹" + rs.getFloat("balance"));
            } else {
                balance_label.setText("Balance available : ₹0");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            balance_label.setText("Balance available : ₹0");
        }
        
        // setup navbar
        Main.getInstance().setupNavbar(navbar);
        
        // initialize required
        updateRequired();
        // toggle button setup
        price_type_toggle.setOnAction(e -> toggleMarketPrice());
        // price quantity update
        quantity_textfield.textProperty().addListener((observable, oldValue, newValue) -> updateRequired());
        price_textfield.textProperty().addListener((observable, oldValue, newValue) -> updateRequired());
        // execute order

    }
    
    @FXML
    private void showTable(int days){
        stock_chart.getData().clear();
        drawChart(stock_chart, days);
    }

    void drawChart(LineChart<String, Number> chart, int days) {
        // Draw the chart
        chart.getXAxis().setLabel("Time");
        NumberAxis YAxis = (NumberAxis) chart.getYAxis();
        chart.getYAxis().setLabel("Price");
        YAxis.setAutoRanging(false);
        
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        ObservableList<XYChart.Data<String, Number>> dataList = FXCollections.observableArrayList();
        // Fetch the data from the database
        ResultSet rs = DatabaseConnection.getInstance().executeQuery(
            "SELECT Symbol, Timestamp, Close " +
            "FROM stock_price " +
            "WHERE Timestamp >= DATE_SUB(NOW(), INTERVAL " + days + " DAY) " +
            "AND HOUR(Timestamp) IN (10,12,14) AND MINUTE(Timestamp) IN (0,30) " +
            "AND Symbol = '" + stock_symbol + "' "+
            "ORDER BY Timestamp asc;"
        );
        // Add the data to the chart
        float min= 100000000f, max= 0;
        try {
            while (rs.next()){
                String timestamp = rs.getString("Timestamp");
                float close = rs.getFloat("Close");
                if (close < min) {
                    min = close;
                }
                if (close > max) {
                    max = close;
                }
                dataList.add(new XYChart.Data<>(timestamp, close));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        chart.setCreateSymbols(false);
        YAxis.setLowerBound(min-10);
        YAxis.setUpperBound(max+10);
        dataSeries.setData(dataList);
        XYChart.Data<String, Number> firstDataPoint = dataSeries.getData().get(0);
        XYChart.Data<String, Number> lastDataPoint = dataSeries.getData().get(dataSeries.getData().size() - 1);
        // if (firstDataPoint.getYValue().doubleValue() - lastDataPoint.getYValue().doubleValue()){
        //     profit_label.setText("Profit: ₹" + (lastDataPoint.getYValue().doubleValue() - firstDataPoint.getYValue().doubleValue()));
        // } else {
        //     profit_label.setText("Loss: ₹" + (firstDataPoint.getYValue().doubleValue() - lastDataPoint.getYValue().doubleValue()));
        // }

        chart.getData().add(dataSeries);
        if (0>firstDataPoint.getYValue().doubleValue() - lastDataPoint.getYValue().doubleValue()){
            dataSeries.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #00cc00;");
        }
        else{
            dataSeries.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: red;");
        }
    }

    @FXML 
    void toggleMarketPrice(){
        if (price_type_toggle.getText().equals("Market")){ 
            price_type_toggle.setText("Limit");
            price_textfield.setDisable(false);
        } else {
            price_type_toggle.setText("Market");
            price_textfield.setDisable(true);
            price_textfield.setText(String.valueOf(price));
        }
    }

    @FXML
    private void updateRequired(){
        String price_text = price_textfield.getText();
        String quantity_text = quantity_textfield.getText();
        if (!quantity_text.equals("") && !price_textfield.getText().equals("")){
            quantity = Integer.parseInt(quantity_text);
            offered_price = Float.parseFloat(price_text);
        }
        else{
            quantity = 0;
            offered_price = 0;
        }
        required_balance = offered_price*quantity;
        required_label.setText("Required : ₹" + required_balance);
    }

    @FXML
    private void placeBuyOrder(){
        if (required_balance == 0){
            return;
        }
        if (required_balance > 0){
            try {
                ResultSet rs = DatabaseConnection.getInstance().executeQuery(
                    "SELECT balance FROM user WHERE email = '" + Session.getUsername() + "';"
                );
                if (rs.next()) {
                    float balance = rs.getFloat("balance");
                    if (balance < required_balance){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Balance isn't enough!");
                        alert.setHeaderText("Oops, You're not rich enough!");
                        alert.setContentText("Please add more funds to your account to place this order.");
                        alert.showAndWait();
                        return;
                    }
                    else{
                        
                        if (price_type_toggle.getText().equals("Market")){
                            DatabaseConnection.getInstance().executeUpdate(
                            "INSERT INTO orders (user_id, symbol, price, quantity, order_type, order_class, time, order_status) VALUES (" +
                            Session.getId() + ", '" + stock_symbol + "', " + offered_price + ", " + quantity + ", 'buy', 'market', NOW() ,'executed');"
                            );
                            Session.addToHoldings(stock_symbol, quantity, offered_price);
                        }
                        
                        else{
                            DatabaseConnection.getInstance().executeUpdate(
                            "INSERT INTO orders (user_id, symbol, price, quantity, order_type, order_class, time, order_status) VALUES (" +
                            Session.getId() + ", '" + stock_symbol + "', " + offered_price + ", " + quantity + ", 'buy', 'limit', NOW() ,'active');"
                            );
                        }
                        
                        DatabaseConnection.getInstance().executeUpdate(
                            "UPDATE user SET balance = balance - " + required_balance + " WHERE email = '" + Session.getUsername() + "';"
                        );
                        balance_label.setText("Balance available : ₹" + (balance - required_balance));
                        
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Order Placed!");
                        alert.setHeaderText("Your order for " + quantity + " shares of " + stock_name + " has been placed successfully!");
                        alert.setContentText("It show up in you Investments as soon as it is executed.");
                        alert.showAndWait();
                        return;
                    }
                } 
                else {
                    System.out.println("User not found");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void placeSellOrder(){
        if (required_balance > 0){
            try {
                ResultSet rs = DatabaseConnection.getInstance().executeQuery(
                    "SELECT balance FROM user WHERE email = '" + Session.getUsername() + "';"
                );
                if (rs.next()) {
                    float balance = rs.getFloat("balance");
                    if (balance < required_balance){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Balance isn't enough!");
                        alert.setHeaderText("Oops, You're not rich enough!");
                        alert.setContentText("Please add more funds to your account to place this order.");
                        alert.showAndWait();
                        return;
                    }
                    else{
                        DatabaseConnection.getInstance().executeUpdate(
                            "INSERT INTO orders (user_id, symbol, price, quantity, type) VALUES (" +
                            Session.getId() + ", '" + stock_symbol + "', " + offered_price + ", " + quantity + ", 'sell');"
                        );
                        DatabaseConnection.getInstance().executeUpdate(
                            "UPDATE user SET balance = balance - " + required_balance + " WHERE email = '" + Session.getUsername() + "';"
                        );
                        balance_label.setText("Balance available : ₹" + (balance - required_balance));
                    }
                } else {
                    System.out.println("User not found");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
