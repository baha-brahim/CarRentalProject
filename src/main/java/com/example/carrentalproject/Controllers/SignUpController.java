package com.example.carrentalproject.Controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.carrentalproject.Controllers.DBUtils.changeScene;
import static com.example.carrentalproject.Controllers.DBUtils.signUpUser;

public class SignUpController implements Initializable {
    @FXML
    private TextField nameField;
    @FXML
    private TextField userNameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private Button createAccountButton;
    @FXML
    private Button backToLoginPage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createAccountButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = nameField.getText();
                String userName = userNameField.getText();
                String password = passwordField.getText();
                String confirmPassword = confirmPasswordField.getText();
                String phoneNumber = phoneNumberField.getText();
                if (!password.equals(confirmPassword)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Passwords do not match");
                    alert.showAndWait();
                    return;
                }
                signUpUser(event ,name ,userName ,password ,phoneNumber);
            }
        });
        backToLoginPage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    changeScene(event, "/Fxml/log-in.fxml", "Log in", 600, 400,0, 0,0,null,null,0,null,null,null);
                    System.out.println("hello");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
