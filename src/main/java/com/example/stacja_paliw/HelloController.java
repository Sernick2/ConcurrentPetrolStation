package com.example.stacja_paliw;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField maxStacja;

    @FXML
    private TextField lStan;

    @FXML
    private TextField lKas;

    @FXML
    private TextField pojPal;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    private void onStartButtonClick()
    {
        try {
            int maxS = Integer.parseInt(maxStacja.getCharacters().toString());
            int lS = Integer.parseInt(lStan.getCharacters().toString());
            int lk = Integer.parseInt(lKas.getCharacters().toString());
            int pP = Integer.parseInt(pojPal.getCharacters().toString());
            HelloApplication.dzialanie(maxS, lS, lk, pP);
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Możesz podać tylko liczby w polach!");
            alert.showAndWait();
        }
    }
}