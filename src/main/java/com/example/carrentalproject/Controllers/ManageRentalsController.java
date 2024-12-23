package com.example.carrentalproject.Controllers;

import com.example.carrentalproject.Classes.Rental;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class ManageRentalsController {


    @FXML
    private TableView<Rental> rentalTable;
    @FXML
    private TableColumn<Rental, Integer> rentalIdColumn;
    @FXML
    private TableColumn<Rental, Integer> carIdColumn;
    @FXML
    private TableColumn<Rental, Integer> clientIdColumn;
    @FXML
    private TableColumn<Rental, String> rentalDateColumn;
    @FXML
    private TableColumn<Rental, String> returnDateColumn;
    @FXML
    private TableColumn<Rental, Double> totalCostColumn;
    @FXML
    private TableColumn<Rental, String> pickUpLocationColumn;
    @FXML
    private TableColumn<Rental, String> rentalStateColumn;
    @FXML
    private TableColumn<Rental, Void> rentalActionColumn;
    @FXML
    private Label errorMessage;


    @FXML
    public void initialize() {
        loadRentalsData();
    }
    private void loadRentalsData() {
        ObservableList<Rental> rentalList = FXCollections.observableArrayList();
        String selectSQL = "SELECT * FROM rental";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/carrental", "root", "root");
             PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
             ResultSet resultSet = preparedStatement.executeQuery())
        {
            while (resultSet.next()) {
                int rentalId = resultSet.getInt("rentalId");
                int carId = resultSet.getInt("carId");
                int clientId = resultSet.getInt("clientId");
                String rentalDate = resultSet.getString("rentalDate");
                String returnDate = resultSet.getString("returnDate");
                double totalCost = resultSet.getDouble("totalCost");
                String pickUpLocation = resultSet.getString("pickUpLocation");
                String rentalState = resultSet.getString("rentalState");
                Rental rental = new Rental(rentalId,clientId,carId,rentalDate,returnDate,totalCost,pickUpLocation,rentalState);
                rentalList.add(rental);
            }
            rentalIdColumn.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
            carIdColumn.setCellValueFactory(new PropertyValueFactory<>("carId"));
            clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
            rentalDateColumn.setCellValueFactory(new PropertyValueFactory<>("rentalDate"));
            returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
            totalCostColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
            pickUpLocationColumn.setCellValueFactory(new PropertyValueFactory<>("pickUpLocation"));
            rentalStateColumn.setCellValueFactory(new PropertyValueFactory<>("rentalState"));


            rentalActionColumn.setCellFactory(param -> new TableCell<>() {
                private final Button actionButton = new Button();
                {

                    actionButton.setOnAction(event -> {
                        Rental rental = getTableRow().getItem();
                        if (rental != null) {
                            if (rental.getRentalState().equals("On going")) {
                                updateRentalState(rental.getRentalId(), rental.getCarId(), "Canceled");
                                loadRentalsData();//Refresh table after the update
                            } else {
                                updateRentalState(rental.getRentalId(), rental.getCarId(), "On going");
                                loadRentalsData();//Refresh table after the update
                            }
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty){
                        setGraphic(null);
                    } else {
                        Rental rental = getTableRow().getItem();
                        if (rental != null){
                            if (rental.getRentalState().equals("On going")){
                                actionButton.setText("Cancel");
                                actionButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");

                            } else {
                                actionButton.setText("Resume");
                                actionButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");

                            }
                            actionButton.getStyleClass().add("btn");
                            setGraphic(actionButton);
                        }

                    }
                }
            });
            rentalTable.setItems(rentalList);

        } catch (SQLException e) {
            errorMessage.setText("SQL error loading rentals: "+e.getMessage());

        }
    }


    private void updateRentalState(int rentalId, int carId, String newState) {
        String updateSQL = "UPDATE rental SET rentalState = ? WHERE rentalId = ?";
        String updateSQL2 = "UPDATE car SET carStatus = ? WHERE carId = ?";
        String rentalState;
        String carStatus;

        // Determine rentalState and carStatus based on newState
        if ("On going".equalsIgnoreCase(newState)) {
            rentalState = "On going"; // Booked
            carStatus = "B";
        } else if ("Canceled".equalsIgnoreCase(newState)) {
            rentalState = "Canceled"; // Free
            carStatus = "F";
        } else {
            errorMessage.setText("Invalid new state provided.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/carrental", "root", "root");
             PreparedStatement rentalStatement = conn.prepareStatement(updateSQL);
             PreparedStatement carStatement = conn.prepareStatement(updateSQL2)) {

            // Update the rental table
            rentalStatement.setString(1, rentalState);
            rentalStatement.setInt(2, rentalId);
            int rentalRowsAffected = rentalStatement.executeUpdate();

            // Update the car table only if rental update was successful
            if (rentalRowsAffected > 0) {
                carStatement.setString(1, carStatus);
                carStatement.setInt(2, carId);
                System.out.println(carId + " " + newState);
                int carRowsAffected = carStatement.executeUpdate();

                if (carRowsAffected > 0) {
                    errorMessage.setText("Rental and car status updated successfully.");
                } else {
                    errorMessage.setText("Car not found or couldn't update.");
                }
            } else {
                errorMessage.setText("Rental not found or couldn't update.");
            }

        } catch (SQLException e) {
            errorMessage.setText("SQL Error updating rental or car: " + e.getMessage());
        }
    }
}
