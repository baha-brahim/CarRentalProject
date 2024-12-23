package com.example.carrentalproject.Controllers;

import com.example.carrentalproject.Classes.Car;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {
    @FXML private AnchorPane carContainer;
    @FXML private Button myRentalsButton;
    @FXML private Label aboutLabel;
    @FXML private Label infoLabel;
    @FXML private ScrollPane homePageScrollPane;
    @FXML private Button leftButton;
    @FXML private Button rightButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBUtils.populateCarsFromDB(carContainer);
        setupLabelClickHandlers();

        myRentalsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    DBUtils.changeScene((ActionEvent) event,"/Fxml/rental-managment-page.fxml" ,"Rental panagment page" ,819 ,600 ,0 ,0,0 , null,null, 0,null ,null ,null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void setupLabelClickHandlers()
    {

        aboutLabel.setOnMouseClicked(this::handleLabelClick);
        infoLabel.setOnMouseClicked(this::handleLabelClick);

    }

    private void handleLabelClick(javafx.scene.input.MouseEvent mouseEvent) {
        double targetVValue = 1.0;
        double currentVValue = homePageScrollPane.getVvalue();

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(homePageScrollPane.vvalueProperty(), targetVValue);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
}
