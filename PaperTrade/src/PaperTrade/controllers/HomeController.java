package PaperTrade.controllers;

import PaperTrade.Main;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class HomeController {
    @FXML
    private HBox navbar;

    public void initialize() {
        // Initialize the controller
        // Add any initialization logic here
        Main.getInstance().setupNavbar(navbar);
    }
}
