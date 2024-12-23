package com.example.carrentalproject.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class AdminController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private StackPane contentPane;

    @FXML
    public void showAddCar() { setContent("/Fxml/add-car.fxml"); }

    @FXML
    public void showDeleteCar() {
        setContent("/Fxml/delete-car.fxml");
    }

    @FXML
    public void showAddClient() {
        setContent("/Fxml/add-client.fxml");
    }

    @FXML
    public void showDeleteClient() {
        setContent("/Fxml/delete-client.fxml");
    }

    @FXML
    public void showRentals() {
        setContent("/Fxml/manage-rentals.fxml");
    }


    private void setContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll((Node) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            //Here you can add some error messages on the app that notify a loading failure
        }
    }
}