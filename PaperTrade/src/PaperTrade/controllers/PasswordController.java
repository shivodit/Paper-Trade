package PaperTrade.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import PaperTrade.db.DatabaseConnection;
import PaperTrade.models.Session;

public class PasswordController {

    @FXML
    private PasswordField new_password_textfield;
    @FXML
    private Button save;
    @FXML
    private PasswordField confirm_password_textfield;
    private int LoggedInUserID = Session.getId();

    public void saveChanges()
    {
        String new_password = new_password_textfield.getText();
        String confirm_password = confirm_password_textfield.getText();
        if (!new_password.equals(confirm_password))
        {
            System.out.println("Passwords do not match");
            return;
        }
        String query = "UPDATE User SET Password = '" + new_password + "' WHERE user_id = '" + LoggedInUserID + "'";
        DatabaseConnection.getInstance().executeUpdate(query);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Password Changed");
        alert.setHeaderText("Password Changed Successfully");
        alert.showAndWait();
    }
    @FXML
    private void handlesaveButtonClick(MouseEvent event) {
        saveChanges();
    }
}
