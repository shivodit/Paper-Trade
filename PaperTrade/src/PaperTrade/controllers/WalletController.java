package PaperTrade.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;

import PaperTrade.db.DatabaseConnection;
import PaperTrade.models.Session;

import java.sql.ResultSet;
public class WalletController {
    @FXML
    private Label currentbal_label;
    @FXML
    private TextField addfunds;
    @FXML
    private Button save;

    private int LoggedInUserID = Session.getId();
    
    @FXML
    public void saveChanges()
    {
        String updated_user_balance = addfunds.getText();
        String query = "UPDATE User SET Balance = Balance+'" + updated_user_balance + "' WHERE user_id = '" + LoggedInUserID + "'";
        DatabaseConnection.getInstance().executeUpdate(query);
        currentbal_label.setText(""+(Float.parseFloat(currentbal_label.getText()) + Float.parseFloat(updated_user_balance)));
    }
    @FXML
    private void handlesaveButtonClick(MouseEvent event) {
        saveChanges();
    }

    public void initialize()
    {
        ResultSet rs = DatabaseConnection.getInstance().executeQuery("Select * from User where user_id ="+LoggedInUserID);
        try {
            if (rs.next()){

                currentbal_label.setText(rs.getString("Balance"));
            }
            else
            {
                System.out.print("User not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

