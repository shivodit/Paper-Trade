package main.PaperTrade.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoginController{

    @FXML
    private Label label;

    public void initialize() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
    }
}