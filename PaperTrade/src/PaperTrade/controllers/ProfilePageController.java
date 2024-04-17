package PaperTrade.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;

import PaperTrade.Main;
import PaperTrade.db.DatabaseConnection;
import PaperTrade.models.Session;

public class ProfilePageController {
    @FXML
    private Label user_id;
    @FXML
    private Label User_name_label;
    @FXML
    private TextField name_textfield;
    @FXML
    private TextField dob_textfield;
    @FXML
    private TextField email_textfield;
    @FXML
    private ListView<TextField> phone_no_lv;
    @FXML
    private Button save;
    @FXML
    private  Label changepass_label;
    @FXML
    private  Label Reports_label;
    @FXML
    private Label wallet_label;
    @FXML
    private Button Back_button;
    @FXML
    private HBox navbar;
    
    private int LoggedInUserID=Session.getId();

    private ArrayList<TextField> phoneNumbers = new ArrayList<TextField>();
    public void addPhoneNumber()
    {
        DatabaseConnection db = DatabaseConnection.getInstance();
        String query = "select Phone_no from contact where User_ID = '" + LoggedInUserID + "'";
        ResultSet rs = db.executeQuery(query);
        try {
            while(rs.next())
            {
                TextField tf = new TextField();
                tf.setText(rs.getString("Phone_no"));
                phoneNumbers.add(tf);
                phone_no_lv.getItems().add(tf);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void populate_user_data()
    {
        ResultSet rs = DatabaseConnection.getInstance().executeQuery("Select * from User where user_id ="+LoggedInUserID);// Change

        try {
            if (rs.next()){

               User_name_label.setText(rs.getString("Name"));
               name_textfield.setText(rs.getString("Name"));
               email_textfield.setText(rs.getString("Email"));
               dob_textfield.setText(rs.getString("DOB"));
               user_id.setText("User UID : #"+rs.getString("User_ID"));

            }
            else
            {
                System.out.print("User not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveChanges()
    {
        String updated_user_name = name_textfield.getText();
        String updated_dob = dob_textfield.getText();
        String updated_email = email_textfield.getText();
        String query = "UPDATE User SET Name = '" + updated_user_name + "', DOB = '" + updated_dob + "', Email = '" + updated_email + "' WHERE user_id = '" + LoggedInUserID + "'";
        DatabaseConnection.getInstance().executeUpdate(query);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Profile Updated");
        alert.setHeaderText("Profile Updated Successfully");
        alert.showAndWait();
    }
    @FXML
    private void handlesaveButtonClick(MouseEvent event) {
       saveChanges();
    }
    @FXML
    void openPopup(MouseEvent event) {
        try {
           // System.out.println("fired");//
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/PaperTrade/views/wallet.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Wallet Window");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
}
    @FXML
    void openPopup2(MouseEvent event) {
        try {
           // System.out.println("fired");//
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/PaperTrade/views/pass.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Password Window");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void initialize()
    {
        populate_user_data();
        addPhoneNumber();
        Main.getInstance().setupNavbar(navbar);
    }
}