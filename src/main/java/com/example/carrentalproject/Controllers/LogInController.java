package com.example.carrentalproject.Controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.carrentalproject.Controllers.DBUtils.*;

public class LogInController implements Initializable {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button logInButton;
    @FXML
    private Button signUpButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    logInUser(event , usernameField.getText() , passwordField.getText());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    changeScene(event, "/Fxml/sign-up.fxml", "Sign up", 600, 400,0, 0 ,0,null,null,0,null,null,null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}