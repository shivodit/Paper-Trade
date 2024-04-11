package PaperTrade.controllers;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import PaperTrade.Main;
import PaperTrade.db.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class RegisterController {
    @FXML
    private Button register;
    
    @FXML
    private TextField inp_name;
    
    @FXML 
    private ListView<TextField> inp_phone_no;
    
    @FXML
    private Button add_number_button;
    
    @FXML
    private Button remove_number_button;

    @FXML
    private TextField inp_email;
    
    @FXML
    private PasswordField inp_password1;
    
    @FXML
    private PasswordField inp_password2;
    
    @FXML
    private DatePicker inp_dob;

    @FXML
    private Button back;


    private ArrayList<TextField> phoneNumbers = new ArrayList<TextField>();
    
    public void initialize() {
        // Initialize the controller
        // Add any initialization logic here
        addPhoneNumber(null);
        back.setOnMouseClicked(e ->
        {
            try {
                Main.getInstance().goToLogin();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        );
        
    }
    
    @FXML
    private void handleRegisterButtonClick(MouseEvent event) {
        // Handle the register button click event
        // Add your code here
        String email = inp_email.getText();
        String password1 = inp_password1.getText();
        String password2 = inp_password2.getText();
        String name = inp_name.getText();
        String dob = inp_dob.getValue().toString();
        
        if (!password1.equals(password2)) {
            System.out.println("Passwords do not match");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Invalid Information");
            alert.setHeaderText("The passwords do not match");
            alert.setContentText("please carefully enter the same password in both fields");
            alert.showAndWait();
            return;
        }
        // validate email
        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("Invalid email");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Invalid Information");
            alert.setHeaderText("The email is invalid");
            alert.setContentText("please carefully enter a valid email");
            alert.showAndWait();
            return;
        }
        // age validation
        System.out.println(dob);
        if (Integer.parseInt(dob.split("-")[0]) > 2005) {
            System.out.println("Invalid age");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Invalid Information");
            alert.setHeaderText("You are too young to register");
            alert.setContentText("You must be atleast 18 years old to register");
            alert.showAndWait();
            return;
        }
        // phone number validation
        for (TextField tf : phoneNumbers) {
            if (tf.getText().length() < 10) {
                System.out.println("Invalid phone number");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Invalid Information");
                alert.setHeaderText("The phone number is invalid");
                alert.setContentText("please carefully enter a valid phone number");
                alert.showAndWait();
                return;
            }
        }
        // check if email already exists
        DatabaseConnection db = DatabaseConnection.getInstance();
        String query = "SELECT email FROM user WHERE email = '" + email + "'";
        ResultSet rs = db.executeQuery(query);
        try {
            if (rs.next()) {
                System.out.println("Email already exists");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Invalid Information");
                alert.setHeaderText("The email already exists");
                alert.setContentText("please carefully enter a different email");
                alert.showAndWait();
                return;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Add the user to the database
        String insertquery = "INSERT INTO user (name, email, password, dob) VALUES ('" + name + "', '" + email + "', '" + password1 + "', '" + dob + "')";
        db.executeUpdate(insertquery);

        // Add the phone numbers to the database
        for (TextField tf : phoneNumbers) {
            query = "INSERT INTO contact (user_id, Phone_no) VALUES ((SELECT user_id FROM user WHERE email = '" + email + "'), '" + tf.getText() + "')";
            db.executeUpdate(query);
        }
        System.out.println("User registered successfully");
    }
    

    @FXML 
    private void addPhoneNumber(MouseEvent event) {
        // Code to add phone number textfield to the listview inp_phone_number
        TextField tf = new TextField();
        phoneNumbers.add(tf);
        inp_phone_no.getItems().add(tf);
    }

    @FXML
    public void removePhoneNumber(MouseEvent event) {
        // Code to remove phone number textfield from the listview inp_phone_number
        if (phoneNumbers.size() > 0) {
            inp_phone_no.getItems().remove(phoneNumbers.size() - 1);
            phoneNumbers.remove(phoneNumbers.size() - 1);
        }
    }

}
