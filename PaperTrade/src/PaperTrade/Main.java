package PaperTrade;

import java.io.IOException;
import java.lang.ModuleLayer.Controller;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import PaperTrade.controllers.LoginController;
import PaperTrade.controllers.RegisterController;

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
        
        LoginController l = loader.getController();
        stage = primaryStage;
        // initialize the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Welcome to Paper Trade");
        primaryStage.show();
    }

    @FXML
    public void goToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PaperTrade/views/login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        LoginController l = loader.getController();
    }
    @FXML
    public void goToRegister() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PaperTrade/views/register.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        RegisterController l = loader.getController();
    }

    
    public static void main(String[] args) {
        launch(args);
    }
}