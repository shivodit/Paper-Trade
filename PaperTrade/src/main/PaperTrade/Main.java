package main.PaperTrade;

import java.lang.ModuleLayer.Controller;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.PaperTrade.controllers.LoginController;
import main.PaperTrade.db.DatabaseConnection;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/PaperTrade.fxml"));
        primaryStage.setTitle("Welcome to PaperTrade!");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        LoginController controller = new LoginController();
        controller.initialize();
        // TEST CODE DONOT RELEASE
        DatabaseConnection db = DatabaseConnection.getInstance();
        ResultSet rs = db.executeQuery("SELECT * FROM user");
        try {
            while (rs.next()) {
                try {
                    System.out.println(rs.getString("email"));
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}