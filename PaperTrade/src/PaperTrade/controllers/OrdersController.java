package PaperTrade.controllers;

import PaperTrade.models.Order;
import PaperTrade.models.Session;
import PaperTrade.Main;
import PaperTrade.db.*;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.Timestamp;

public class OrdersController {

    /*public void goToOrders(ActionEvent event) throws IOException {
        Parent ordersParent = FXMLLoader.load(getClass().getResource("orders.fxml"));
        Scene ordersScene = new Scene(ordersParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(ordersScene);
        window.show();
    }*/

 
    @FXML
    private TableView<Order> ordersTable;
    @FXML
    private TableColumn<Order, Integer> idColumn;
    @FXML
    private TableColumn<Order, String> stockColumn;
    @FXML
    private TableColumn<Order, Integer> quantityColumn;
    @FXML
    private TableColumn<Order, Float> priceColumn;
    @FXML
    private TableColumn<Order, Timestamp> timeColumn;
    @FXML
    private TableColumn<Order, String> typeColumn;
    @FXML
    private TableColumn<Order, String> classColumn;
    @FXML
    private TableColumn<Order, String> statusColumn;
    @FXML 
    private HBox navbar;

    @FXML
    public void initialize() {
        loadOrdersFromDatabase();
        Main.getInstance().setupNavbar(navbar);
    }

    private void loadOrdersFromDatabase() {
        ObservableList<Order> orders = FXCollections.observableArrayList();

        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            String query = "select * from stock_order where User_ID = " + Session.getId() + " ORDER BY order_status asc, time desc;";
            ResultSet rs = db.executeQuery(query);
            while (rs.next()) {
                Order order = new Order(
         
                    rs.getInt("Order_ID"),
                    rs.getString("Symbol"), //there is no stock name in stock_order table
                    rs.getInt("Quantity"),
                    rs.getFloat("Price"),
                    rs.getTimestamp("Time"),
                    rs.getString("Order_type"),
                    rs.getString("Order_class"),
                    rs.getString("Order_status")
                );
                orders.add(order);
            }

            
        } catch (Exception e) {
            e.printStackTrace();
        }  
            
        idColumn.setCellValueFactory(new PropertyValueFactory<>("order_id"));  
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("order_type"));  
        classColumn.setCellValueFactory(new PropertyValueFactory<>("order_class"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("order_status"));

        //ordersTable.getColumns().setAll(idColumn, stockColumn, quantityColumn, priceColumn, timeColumn, typeColumn, classColumn, statusColumn);
        ordersTable.setItems(orders);

        ordersTable.setRowFactory(tv -> {
            TableRow<Order> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem menuItem = new MenuItem("Remove Order");

            menuItem.setOnAction(event -> {
                Order order = row.getItem();
                try {
                    DatabaseConnection db = DatabaseConnection.getInstance();
                    String query = "UPDATE stock_order SET Order_status = 'cancelled' WHERE Order_ID = " + order.getOrder_id() + ";";
                    db.executeUpdate(query);
                    order.setOrder_status("cancelled");
                    loadOrdersFromDatabase();
                    if (order.getOrder_type().equals("buy")) {
                        query = "UPDATE user SET Balance = Balance + " + order.getPrice() * order.getQuantity() + " WHERE User_ID = " + Session.getId() + ";";
                    } else {
                        query = "UPDATE holds SET Quantity = Quantity + " + order.getQuantity() + " WHERE User_ID = " + Session.getId() + ";";
                    }
                    db.executeUpdate(query);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Order cancellation success");
                    alert.setHeaderText("Your order has been successfully cancelled");
                    alert.setContentText("Your assests have been refunded to your account.");
                    alert.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            contextMenu.getItems().add(menuItem);
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty() && row.getItem().getOrder_status().equals("active")) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            return row;
        });

    } 
}