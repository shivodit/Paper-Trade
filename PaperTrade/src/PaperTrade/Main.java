package PaperTrade;

import java.io.IOException;
import java.sql.ResultSet;

import javafx.scene.Node;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import PaperTrade.controllers.LoginController;
import PaperTrade.controllers.RegisterController;
import PaperTrade.db.DatabaseConnection;
import PaperTrade.models.Session;

public class Main extends Application {
    Stage stage;
    private static Main instance;
    public Main(){
        instance = this;
    }
    public static Main getInstance(){
        if (instance == null){
            instance = new Main();
        }
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        stage = primaryStage;
        // initialize the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Welcome to Paper Trade");
        primaryStage.show();
        primaryStage.centerOnScreen();

        // fast login for testing TEST 
        Session.loginUsername("Emma.Smith@gmail.com", 1);
        goToHome();
        
    }

    @FXML
    public void goToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PaperTrade/views/login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        LoginController l = loader.getController();
        stage.centerOnScreen();

    }

    @FXML
    public void goToRegister() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PaperTrade/views/register.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        RegisterController l = loader.getController();
        stage.centerOnScreen();

    }

    @FXML
    public void openStock(String stock_symbol) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PaperTrade/views/stock_view.fxml"));
        PaperTrade.controllers.StockController cont = new PaperTrade.controllers.StockController(stock_symbol);
        loader.setController(cont);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    public void goToInvestments() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PaperTrade/views/investments.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    public void goToHome() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PaperTrade/views/home.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        System.out.println("Going to home");
    }

    @FXML
    public void search(Node search_box_node) throws IOException {
        TextField search_box = (TextField)search_box_node;
        String search_string = ((javafx.scene.control.TextField)search_box).getText();
        ContextMenu contextMenu = new ContextMenu();
        try{
            String s = "SELECT * FROM stock WHERE symbol LIKE '%" + search_string + "%' OR name LIKE '%" + search_string + "%'";
            ResultSet rs = DatabaseConnection.getInstance().executeQuery(s);
            while (rs.next()){
                String symbol = rs.getString("symbol");
                MenuItem item = new MenuItem(rs.getString("name") + " (" + symbol + ")");
                item.setOnAction(e -> {
                    try {
                        openStock(symbol);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                contextMenu.getItems().add(item);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Point2D p = search_box_node.localToScreen(search_box_node.getLayoutBounds().getMinX(), search_box_node.getLayoutBounds().getMaxY());
        contextMenu.show(search_box_node, p.getX() , p.getY());
    }

    @FXML
    public void goToProfile() throws IOException {
        // FXMLLoader loader = new FXMLLoader(getClass().getResource("/PaperTrade/views/profile.fxml"));
        // Parent root = loader.load();
        // Scene scene = new Scene(root);
        // stage.setScene(scene);
        // stage.centerOnScreen();
        System.out.println("Going to profile");
    }

    @FXML
    public void goToOrders() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PaperTrade/views/orders.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    public void goToWatclist() {
        System.out.println("Going to watchlist");
    }
    @FXML
    public void goToTutorial() {
        System.out.println("Going to tutorial");
    }
    @FXML
    public void logout(){
        Session.logoutUsername();
        try {
            goToLogin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupNavbar(HBox navbar) {
        ObservableList<Node> l = navbar.getChildren();
        // setup home button 
        l.get(0).setOnMouseClicked(e -> {
            try {
                goToHome();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        // setup investments button
        l.get(1).setOnMouseClicked(e -> {
            try {
                goToInvestments();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // setup search button
        l.get(4).setOnMouseClicked(e->{
            try {
                search(l.get(3));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        // setup profile button
        ObservableList<MenuItem> profile_dropdown = ((javafx.scene.control.MenuButton)l.get(5)).getItems();
        profile_dropdown.get(0).setOnAction(e -> {
            try {
                goToProfile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        profile_dropdown.get(1).setOnAction(e -> {
            try {
                goToOrders();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        profile_dropdown.get(2).setOnAction(e->{
            goToWatclist();
        });
        profile_dropdown.get(3).setOnAction(e->{
            goToTutorial();
        });
        profile_dropdown.get(4).setOnAction(e->{
            logout();
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}