package com.example.carrentalproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import java.util.Random;
import java.sql.*;

public class AddCarController {

    @FXML
    private TextField carModelField;
    @FXML
    private TextField carBrandField;
    @FXML
    private TextField carYearField;
    @FXML
    private TextField carUrlField;
    @FXML
    private TextField carCostField;
    @FXML
    private TextField carDescField;
    @FXML
    private TextField carNbSeatsField;
    @FXML
    private TextField carTransTypeField;
    @FXML
    private Label errorMessage;


    @FXML
    public void saveCar() {
        try {
            // Reset error messages
            errorMessage.setText("");

            String carModel = carModelField.getText().trim();
            String carBrand = carBrandField.getText().trim();
            String carYear = carYearField.getText().trim();
            String carUrl = carUrlField.getText().trim();
            String carCost = carCostField.getText().trim();
            String carDesc = carDescField.getText().trim();
            String carNbSeats = carNbSeatsField.getText().trim();
            String carTransType = carTransTypeField.getText().trim();

            // Validate required fields
            if(carModel.isEmpty()||carBrand.isEmpty() || carYear.isEmpty() || carUrl.isEmpty()|| carCost.isEmpty()|| carDesc.isEmpty()|| carNbSeats.isEmpty()|| carTransType.isEmpty()){
                errorMessage.setText("All fields are mandatory !");
                return;
            }
            if (!carYear.matches("\\d+") || !carNbSeats.matches("\\d+")) {
                errorMessage.setText("Year and number of seats must be numeric.");
                return;
            }
            if (!carCost.matches("\\d+(\\.\\d+)?")) {
                errorMessage.setText("The cost must be a valid numeric value.");
                return;
            }
            insertCarIntoDB(carModel, carBrand, Integer.parseInt(carYear), "F",carUrl, Double.parseDouble(carCost), carDesc , Integer.parseInt(carNbSeats),carTransType);
            //Clear form and show success
            clearForm();
        }catch(Exception e){
            errorMessage.setText("Error during car creation, "+e.getMessage());
        }

    }
    private void insertCarIntoDB(String carModel,String carBrand,int carYear,String carStatus,String carUrl,double carCost, String carDesc,int carNbSeats,String carTransType) {

        String insertSQL = "INSERT INTO car (carModel, carBrand, carYear, carStatus, carUrl,carCostPerDay, carDesc, carNbSeats, carTransType) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/carrental", "root", "root");
             PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, carModel);
            preparedStatement.setString(2, carBrand);
            preparedStatement.setInt(3,carYear );
            preparedStatement.setString(4, carStatus);
            preparedStatement.setString(5, carUrl);
            preparedStatement.setDouble(6,carCost );
            preparedStatement.setString(7, carDesc);
            preparedStatement.setInt(8,carNbSeats );
            preparedStatement.setString(9, carTransType);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                errorMessage.setText("Car added Successfully.");
            } else {
                errorMessage.setText("Error Adding the new car.");
            }
        }
        catch (SQLException e) {
            errorMessage.setText("SQL Error adding the new car: " + e.getMessage());

        }
    }

    private void clearForm() {
        carModelField.clear();
        carBrandField.clear();
        carYearField.clear();
        carUrlField.clear();
        carCostField.clear();
        carDescField.clear();
        carNbSeatsField.clear();
        carTransTypeField.clear();
    }

}