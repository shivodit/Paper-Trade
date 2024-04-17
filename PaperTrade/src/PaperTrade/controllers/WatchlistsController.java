package PaperTrade.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import PaperTrade.Main;
import PaperTrade.db.DatabaseConnection;
import PaperTrade.models.Session;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import PaperTrade.models.Session;

public class WatchlistsController {
    
    @FXML private Button newWatchlist_button;
    @FXML private TabPane watchlists;
    @FXML private Button removeWatchlist_button;
    @FXML private HBox navbar;
    private Map<String, Tab> tabMap = new HashMap<>();

    @FXML
    public void initialize() {
        display();   
        Main.getInstance().setupNavbar(navbar);
    }

    @FXML
    public void newWatchlist(MouseEvent event){

        TextInputDialog dialog = new TextInputDialog("Watchlist");
        dialog.setTitle("New Watchlist");
        dialog.setHeaderText("Enter name of the new watchlist:");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if(!isNameExistsInWatchlist(name)){
                Tab newTab = createTab(name); //createTab() method inserts data in mysql database
                watchlists.getTabs().add(newTab);
            }
            else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Watchlist with this name already exists!");
                alert.setContentText("Please enter a different name");
                alert.showAndWait();   
            }
            
        });
    };

    //checks if the name already exists in the watchlist
    private boolean isNameExistsInWatchlist(String name) {
        DatabaseConnection db = DatabaseConnection.getInstance();
        String query = "SELECT COUNT(*) FROM Watchlist WHERE List_Name = '"+ name + "' AND User_ID = " + Session.getId() + ";";
        // String query = "SELECT COUNT(*) FROM Watchlist WHERE List_Name = '"+ name + "' AND User_ID = 5;";
        try {
            ResultSet rs = db.executeQuery(query);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removeWatchlist(MouseEvent event){
        Tab focusedTab = watchlists.getSelectionModel().getSelectedItem();  
        DatabaseConnection db = DatabaseConnection.getInstance();
        db.executeUpdate("DELETE FROM tracks WHERE Watchlist_ID = (SELECT Watchlist_ID FROM Watchlist WHERE List_Name = '"+ focusedTab.getText() + "' AND User_ID = "+ Session.getId()+") ;");
        db.executeUpdate("DELETE FROM Watchlist WHERE List_Name = '"+ focusedTab.getText() + "' AND User_ID = "+ Session.getId() +";");watchlists.getTabs().remove(focusedTab);
        display();
    };

    public Tab createTab(String name){
        Tab tab = new Tab(name);
        tabMap.put(name, tab);
        tab.setText(name);
        DatabaseConnection db = DatabaseConnection.getInstance();
        db.executeUpdate("INSERT INTO Watchlist (User_ID, List_Name) VALUES (" + Session.getId() + ", '" + name + "');");
        // db.executeUpdate("INSERT INTO Watchlist (User_ID, List_Name) VALUES (5, '" + name + "');");
        return tab;
    };

    public HBox createHbox(String symbol, String tabkey, Tab tab) throws SQLException{
        HBox hbox = new HBox();
        hbox.setId("stock_vbox");
        hbox.setPadding(new Insets(10,10,10,10));
        hbox.setSpacing(80);
        hbox.setStyle("-fx-background-color: white;");
        hbox.setMaxHeight(60);// height is yet to be fixed
        hbox.setAlignment(Pos.CENTER);
        hbox.prefWidthProperty().bind(tab.getTabPane().widthProperty());
        hbox.setOnMouseClicked(e->{
            try {
                Main.getInstance().openStock(symbol);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        DatabaseConnection db = DatabaseConnection.getInstance();
        String query = "SELECT * FROM stock WHERE Symbol= '" + symbol+ "';";
        ResultSet rs = db.executeQuery(query);
        rs.next();

        String stock_name = rs.getString("Name");
        Label name = new Label(stock_name);
        hbox.getChildren().add(name);
        
        float price = Session.getMarketPrice(symbol);        
        Label market_price = new Label(""+price);
        hbox.getChildren().add(market_price);

        float price_diff = Session.get1DayChange(symbol);
        float percentage = (-100*price_diff)/(price_diff-price);
        Label one_day_change = new Label(""+ percentage + "%");
        hbox.getChildren().add(one_day_change);

        Button remove_button= new Button("Remove");
        hbox.getChildren().add(remove_button);
        remove_button.setOnMouseClicked(e -> {
            Tab focusedTab = watchlists.getSelectionModel().getSelectedItem();  
            //db.executeQuery("DELETE FROM tracks WHERE Watchlist_ID = "+ tabkey + " AND Symbol = "+ symbol + ";");
            String deleteQuery = "DELETE FROM tracks WHERE Watchlist_ID = (SELECT Watchlist_ID FROM Watchlist WHERE List_Name = '" + focusedTab.getText() + "' AND user_id = "+Session.getId()+ ") AND Symbol = '" + symbol + "' ;";
            db.executeUpdate(deleteQuery);
            hbox.getChildren().clear();
            hbox.setMaxWidth(0);
            hbox.setMaxHeight(0);
        });
        return hbox;
    };

    public void display(){
        DatabaseConnection db = DatabaseConnection.getInstance();
        try {
            String query = "SELECT List_Name FROM Watchlist WHERE Watchlist.User_ID = "+ Session.getId() +";";
            // String query = "SELECT List_Name FROM Watchlist WHERE Watchlist.User_ID = 5;";
            ResultSet rs = db.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("List_Name"); //List_Name is considered unique
                //createTab(rs.getString("List_Name"));
                //watchlists.getTabs().add(tabMap.get("name"));
                Tab tab = new Tab(name);
                tabMap.put(name, tab);
                tab.setText(name);
                watchlists.getTabs().add(tab);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            for (Map.Entry<String, Tab> entry : tabMap.entrySet()) {
                String key = entry.getKey();
                Tab value = entry.getValue();
                VBox vbox = new VBox();
                vbox.setAlignment(Pos.CENTER);
                String q3 = "SELECT * FROM Watchlist natural join tracks WHERE Watchlist.Watchlist_ID = tracks.Watchlist_ID AND Watchlist.User_ID = "+ Session.getId() +" AND list_name = '"+key+"';";
                ResultSet rs = db.executeQuery(q3);
                while (rs.next()) {
                    String symbol = rs.getString("Symbol");
                    String watchlist_name = rs.getString("List_Name");
                    if(key.equals(watchlist_name)){
                        vbox.getChildren().add(createHbox(symbol,key,value));
                    }
                }
                value.setContent(vbox);
            }
            // String query2 = "SELECT * FROM Watchlist natural join tracks WHERE Watchlist.Watchlist_ID = tracks.Watchlist_ID AND Watchlist.User_ID = "+ Session.getId() +";";
            // // String query2 = "SELECT Symbol, Watchlist_ID, List_Name FROM Watchlist natural join tracks WHERE Watchlist.Watchlist_ID = tracks.Watchlist_ID AND Watchlist.User_ID = 5;";
            // ResultSet rs2 = db.executeQuery(query2);
            // while(rs2.next()){
            //     String symbol = rs2.getString("Symbol");
            //     Integer watchlist_id = rs2.getInt("Watchlist_ID");
            //     String watchlist_name = rs2.getString("List_Name");
            //     for (Map.Entry<String, Tab> entry : tabMap.entrySet()) {
            //         String key = entry.getKey();
            //         Tab value = entry.getValue();
            //         if(key.equals(watchlist_name)){
            //             value.setContent(createHbox(symbol,key,value));   
            //         }
            //     }
            // } 
        }catch (SQLException e) {
            e.printStackTrace();
        }
    };
}
    