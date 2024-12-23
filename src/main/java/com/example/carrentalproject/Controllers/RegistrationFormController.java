package com.example.carrentalproject.Controllers;


// RegistrationFormController.java
import com.dlsc.formsfx.model.structure.DateField;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static com.example.carrentalproject.Controllers.DBUtils.changeScene;

public class RegistrationFormController implements Initializable {
    @FXML private Label carDescLabel;
    @FXML private Label carBrandLabel;
    @FXML private Label carModelLabel;
    @FXML private Label carNbSeatsLabel;
    @FXML private Label carTransTypeLabel;
    @FXML private ImageView carUrlImage;

    @FXML private ComboBox pickUpLocationComboBox;
    @FXML private DatePicker pickUpDateField;
    @FXML private DatePicker returnDateField;
    @FXML private RadioButton cardRadioButton;
    @FXML private RadioButton payOnPickUpRadioButton;
    @FXML private Button rentButton1;
    @FXML private Button backButton1;

    @FXML private TextField cardNameField;
    @FXML private TextField cardNumberField;
    @FXML private TextField expDateField ;
    @FXML private TextField cvcField;
    @FXML private Button rentButton2;
    @FXML private Button backButton2;
    private int carId;
    private int clientId;
    private int carCostPerday;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ToggleGroup toggleGroup = new ToggleGroup();
        cardRadioButton.setToggleGroup(toggleGroup);
        payOnPickUpRadioButton.setToggleGroup(toggleGroup);
        pickUpLocationComboBox.getItems().addAll("Aéroport Tunis Carthage", "Aéroport Nfidha", "Aéroport Gafsa");

        backButton1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    DBUtils.changeScene(event, "/Fxml/Home-page.fxml", "Welcome Back!", 804, 648,0, 0 ,0,null,null,0,null,null,null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        rentButton1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StringBuilder errors = new StringBuilder();

                if (pickUpLocationComboBox.getValue() == null) {
                    errors.append("Select a Location for the pick up\n");
                }

                if (pickUpDateField.getValue() == null) {
                    errors.append("Birth date is required\n");
                }

                if (returnDateField.getValue() == null) {
                    errors.append("Birth date is required\n");
                }

                if (!cardRadioButton.isSelected() && !payOnPickUpRadioButton.isSelected()) {
                    errors.append("A payment method is required\n");
                }

                if (getTotalCost(pickUpDateField ,returnDateField ,carCostPerday) < 0) {
                    errors.append("Dates invalid\n");
                }

                if (errors.length() > 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Validation Error");
                    alert.setHeaderText("Please fix the following errors:");
                    alert.setContentText(errors.toString());
                    alert.showAndWait();
                } else {
                    if (cardRadioButton.isSelected()) {
                        goToCardPage();
                        // Managing the form
                        backButton2.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                // Disabling the interface's element and setting their opacity to 0
                                goToDefaultPage();
                            }
                        });

                        rentButton2.setOnAction(new EventHandler() {
                            @Override
                            public void handle(Event event) {
                                StringBuilder errors = new StringBuilder();

                                if (cardNameField.getText().isEmpty()) {
                                    errors.append("Card name is required\n");
                                }
                                if (cardNumberField.getText().isEmpty()) {
                                    errors.append("Card number is required\n");
                                }
                                if (expDateField.getText().isEmpty()) {
                                    errors.append("Expiration date is required\n");
                                }
                                if (cvcField.getText().isEmpty()) {
                                    errors.append("CVC is required\n");
                                }
                                if (getTotalCost(pickUpDateField ,returnDateField ,carCostPerday) <= 0) {
                                    errors.append("Dates invalid\n");
                                }
                                if (errors.length() > 0) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Validation Error");
                                    alert.setHeaderText("Please fix the following errors:");
                                    alert.setContentText(errors.toString());
                                    alert.showAndWait();
                                } else {
                                    // Setting the total cost
                                    int totalCost = getTotalCost(pickUpDateField, returnDateField, carCostPerday);
                                    System.out.println(clientId);

                                    // Inserting in the rental
                                    DBUtils.fillRental(carId, clientId, pickUpDateField.getValue(), returnDateField.getValue(), totalCost, pickUpLocationComboBox.getValue().toString());

                                    try {
                                        DBUtils.changeScene((ActionEvent) event,"/Fxml/rental-managment-page.fxml" ,"Rental panagment page" ,819 ,600 ,clientId ,carId ,totalCost , null,null, 0,carModelLabel.getText() ,null ,null);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                            }
                        });
                    }else {
                        int totalCost = getTotalCost(pickUpDateField, returnDateField, carCostPerday);

                        DBUtils.fillRental(carId, clientId, pickUpDateField.getValue(), returnDateField.getValue(), totalCost, pickUpLocationComboBox.getValue().toString());
                        try {
                            DBUtils.changeScene(event ,"/Fxml/rental-managment-page.fxml" ,"Rental panagment page" ,819 ,600 ,clientId ,carId ,totalCost , null,null, 0,carModelLabel.getText() ,null ,null);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }
        });
    }

    private int getTotalCost(DatePicker pickUpDateField ,DatePicker returnDateField ,int carCostPerday) {
        if (pickUpDateField.getValue() == null && returnDateField.getValue() == null) {
            return -1;
        }
            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(
                pickUpDateField.getValue(),
                returnDateField.getValue()
        );

        int totalCost = (int) daysBetween * carCostPerday;

        return totalCost;

    }


    @FXML
    public void setChosenCarData (int clientId ,int carId ,int carCostPerday,String carUrl, String carBrand ,int carNbSeats, String carModel, String
    carTransType, String carDesc){
        carUrlImage.setImage(new javafx.scene.image.Image(carUrl));
        carBrandLabel.setText(carBrand);
        carModelLabel.setText(carModel);
        carNbSeatsLabel.setText(String.valueOf(carNbSeats));
        carTransTypeLabel.setText(carTransType);
        carDescLabel.setText(carDesc);
        this.carId = carId;
        this.clientId = clientId;
        this.carCostPerday = carCostPerday;
    }

    public void goToCardPage () {
        // Disabling the interface's element and setting their opacity to 0
        pickUpLocationComboBox.setDisable(true);
        pickUpLocationComboBox.setOpacity(0);
        pickUpDateField.setDisable(true);
        pickUpDateField.setOpacity(0);
        returnDateField.setDisable(true);
        returnDateField.setOpacity(0);
        cardRadioButton.setDisable(true);
        cardRadioButton.setOpacity(0);
        payOnPickUpRadioButton.setDisable(true);
        payOnPickUpRadioButton.setOpacity(0);
        rentButton1.setDisable(true);
        rentButton1.setOpacity(0);
        backButton1.setDisable(true);
        backButton1.setOpacity(0);

        // Making the new interface visible
        cardNameField.setDisable(false);
        cardNameField.setOpacity(1);
        cardNumberField.setDisable(false);
        cardNumberField.setOpacity(1);
        expDateField.setDisable(false);
        expDateField.setOpacity(1);
        cvcField.setDisable(false);
        cvcField.setOpacity(1);
        rentButton2.setDisable(false);
        rentButton2.setOpacity(1);
        backButton2.setDisable(false);
        backButton2.setOpacity(1);

    }

    public void goToDefaultPage () {
        pickUpLocationComboBox.setDisable(false);
        pickUpLocationComboBox.setOpacity(1);
        pickUpDateField.setDisable(false);
        pickUpDateField.setOpacity(1);
        returnDateField.setDisable(false);
        returnDateField.setOpacity(1);
        cardRadioButton.setDisable(false);
        cardRadioButton.setOpacity(1);
        payOnPickUpRadioButton.setDisable(false);
        payOnPickUpRadioButton.setOpacity(1);
        rentButton1.setDisable(false);
        rentButton1.setOpacity(1);
        backButton1.setDisable(false);
        backButton1.setOpacity(1);

        // Making the new interface visible
        cardNameField.setDisable(true);
        cardNameField.setOpacity(0);
        cardNumberField.setDisable(true);
        cardNumberField.setOpacity(0);
        expDateField.setDisable(true);
        expDateField.setOpacity(0);
        cvcField.setDisable(true);
        cvcField.setOpacity(0);
        rentButton2.setDisable(true);
        rentButton2.setOpacity(0);
        backButton2.setDisable(true);
        backButton2.setOpacity(0);

    }
}