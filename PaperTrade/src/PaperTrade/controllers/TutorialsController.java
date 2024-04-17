package PaperTrade.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import PaperTrade.Main;
import PaperTrade.db.DatabaseConnection;
import PaperTrade.models.Session;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.awt.Desktop;
import java.net.URI;

public class TutorialsController {
    @FXML 
    VBox display_area;
    @FXML
    private HBox navbar;

    @FXML
    public void initialize() {
        System.out.println("Tutorials Controller Initialized");
        display();
        Main.getInstance().setupNavbar(navbar);
    }

    private void updateRating(int tutorial_id, int user_id, int rating){
        DatabaseConnection db = DatabaseConnection.getInstance();
        db.executeUpdate("INSERT INTO takes (tutorial_id, user_id, rating) VALUES (" + tutorial_id + ", " + user_id + ", " + rating + ") ON DUPLICATE KEY UPDATE rating = " + rating);
    }

    public VBox createVbox(int Tut_id,String tit, String Hyperlink, String Desc) throws SQLException{
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(15, 20, 15, 20));
        VBox.setMargin(vbox, new Insets(10));
        BackgroundFill backgroundFill = new BackgroundFill(Color.WHITE, null, null);
        Background background = new Background(backgroundFill);
        vbox.setBackground(background);
        vbox.setStyle("-fx-border-width : 2;");
        
        Label title = new Label(tit);
        Font boldFont = Font.font("Arial", FontWeight.BOLD, 14);
        title.setFont(boldFont);
        vbox.getChildren().add(title);
        
       
        Label description = new Label(Desc);
        description.setMaxWidth(600);
        description.setWrapText(true);
        vbox.getChildren().add(description);

        Hyperlink link = new Hyperlink(Hyperlink);
        vbox.getChildren().add(link);
        link.setOnAction(event -> {
            try {
                // URL is opened in the default web browser
                Desktop.getDesktop().browse(new URI(Hyperlink));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        Label rate_label = new Label("Rate this tutorial! ");
        Slider slider = new Slider(1, 5, getRating(Tut_id,Session.getId()));
        slider.setBlockIncrement(1);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setSnapToTicks(true);
        slider.setOrientation(Orientation.HORIZONTAL);
        slider.setMaxWidth(300);
        vbox.getChildren().add(rate_label);

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateRating(Tut_id,Session.getId(),newValue.intValue());
        });

        vbox.getChildren().add(slider);
        return vbox;
    }

    private int getRating(int tut_id, int id) {
        DatabaseConnection db = DatabaseConnection.getInstance();
        ResultSet rs = db.executeQuery("SELECT rating FROM takes WHERE tutorial_id = " + tut_id + " AND user_id = " + id);
        try {
            if(rs.next()){
                return rs.getInt("rating");
            }
        } catch (SQLException e) {
            return 0;
        }
        return 0;
    }

    public void display() {
        DatabaseConnection db = DatabaseConnection.getInstance();
        ResultSet rs = db.executeQuery("SELECT * FROM tutorial");
        try{
            while(rs.next()){
                display_area.getChildren().add(createVbox(
                    rs.getInt("tutorial_id"),
                    rs.getString("title"),
                    rs.getString("hyperlink"),
                    rs.getString("description")
                ));
            }
        }catch(SQLException e){
            e.printStackTrace();
        } 
    }
}
