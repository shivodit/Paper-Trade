package PaperTrade.controllers;

import java.sql.ResultSet;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import PaperTrade.Main;
import PaperTrade.db.DatabaseConnection;
import PaperTrade.models.Session;

public class LoginController{

    @FXML
    TextField email_textbox;
    @FXML
    PasswordField password_textbox;
    @FXML
    Button login_button;
    @FXML
    Button register_button;

    public void initialize() {
        // Initialize the controller
        // Add any initialization logic here
        register_button.setOnMouseClicked(e -> {
            try {
                Main.getInstance().goToRegister();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    public void login(String email, String password){
        ResultSet r = DatabaseConnection.getInstance().executeQuery("SELECT * FROM user WHERE email = '" + email + "' AND password = '" + password + "'");
        try {
            if (r.next()){
                // login 
                Session.loginUsername(r.getString("Email"),r.getInt("user_id"));
                System.out.println("Logged in as " + r.getString("Email"));
                Main.getInstance().goToHome();
            }
            else{
                // wrong username password
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Authentication Failed");
                alert.setHeaderText("Ooops, there was an error!");
                alert.setContentText("Please check Email and password Carefully!");
                alert.showAndWait();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoginButtonAction(MouseEvent event) {
        String email = email_textbox.getText();
        String password = password_textbox.getText();
        login(email, password);
    }

}