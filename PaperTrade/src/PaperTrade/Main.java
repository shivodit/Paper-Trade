package PaperTrade;

import java.lang.ModuleLayer.Controller;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import PaperTrade.controllers.LoginController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        LoginController controller = loader.getController();
        controller.setStage(primaryStage);
        // initialize the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login into Paper Trade");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}