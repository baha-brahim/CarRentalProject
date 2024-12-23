package com.example.carrentalproject.Controllers;

import com.example.carrentalproject.Classes.Rental;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.example.carrentalproject.Controllers.DBUtils.changeScene;

public class RentalManagerController {

    @FXML private TableView<Rental> rentalTableView;
    @FXML private TableColumn<Rental, String> carModelColumn;
    @FXML private TableColumn<Rental, String> rentalDateColumn;
    @FXML private TableColumn<Rental, String> returnDateColumn;
    @FXML private TableColumn<Rental, Double> totalCostColumn;
    @FXML private TableColumn<Rental, String> pickUpLocationColumn;
    @FXML private TableColumn<Rental, String> rentalStateColumn;


    private ObservableList<Rental> rentalDataList = FXCollections.observableArrayList();

    public void initialize() {
        // Set up the columns
        carModelColumn.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        rentalDateColumn.setCellValueFactory(new PropertyValueFactory<>("rentalDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        totalCostColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        pickUpLocationColumn.setCellValueFactory(new PropertyValueFactory<>("pickUpLocation"));
        rentalStateColumn.setCellValueFactory(new PropertyValueFactory<>("rentalState"));

        // Load data from the database
        loadRentalData();
        rentalTableView.setItems(rentalDataList);

    }

    private void loadRentalData() {
        String rentalQuery = null;
        if (SessionManger.getCurrentUserName() == "admin") {
            rentalQuery = "SELECT * FROM rental";
        } else {
            rentalQuery = "SELECT * FROM rental WHERE clientId = ?";
        }
        String carQuery = "SELECT carModel FROM car WHERE carId = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CarRental", "root", "root")) {
            // First PreparedStatement for rental data
            try (PreparedStatement rentalStmt = connection.prepareStatement(rentalQuery)) {
                if (SessionManger.getCurrentUserName() != "admin") {
                    rentalStmt.setInt(1, SessionManger.getCurrentUserId());
                }
                try (ResultSet rentalResultSet = rentalStmt.executeQuery()) {

                    // Iterate through rental results
                    while (rentalResultSet.next()) {
                        int carId = rentalResultSet.getInt("carId");
                        String rentalDate = rentalResultSet.getString("rentalDate");
                        String returnDate = rentalResultSet.getString("returnDate");
                        double totalCost = rentalResultSet.getDouble("totalCost");
                        String pickUpLocation = rentalResultSet.getString("pickUpLocation");
                        String rentalState = rentalResultSet.getString("rentalState");

                        // Fetch carModel for the current carId
                        String carModel = "";
                        try (PreparedStatement carStmt = connection.prepareStatement(carQuery)) {
                            carStmt.setInt(1, carId);
                            try (ResultSet carResultSet = carStmt.executeQuery()) {
                                if (carResultSet.next()) {
                                    carModel = carResultSet.getString("carModel");
                                }
                            }
                        }

                        // Add the data to the list
                        rentalDataList.add(new Rental(carModel, rentalDate, returnDate, totalCost, pickUpLocation, rentalState));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleCancelRental() {
        Rental selectedRental = rentalTableView.getSelectionModel().getSelectedItem();

        if (selectedRental == null) {
            showAlert("Please select a rental to cancel.");
            return;
        }
        if (!selectedRental.getRentalState().equals("On going"))
        {
            showAlert("This rental can not be cancelled");
            return;
        }


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Rental");
        alert.setHeaderText("Confirmation");
        alert.setContentText("Are you sure you want to cancel this rental?");


        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Changing in the db accordingly
            DBUtils.updateRentals(selectedRental);
            selectedRental.setRentalState("Cancelled");
            rentalTableView.refresh();
            showAlert("Rental Cancelled");
        }


    }

    @FXML
    private void handleExtendRental() {
        Rental selectedRental = rentalTableView.getSelectionModel().getSelectedItem();

        if (selectedRental == null) {
            showAlert("Please select a rental to extend.");
            return;
        }
        if (!selectedRental.getRentalState().equals("On going"))
        {
            showAlert("This rental can not be extended");
            return;
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate rentalReturnDate = LocalDate.parse(selectedRental.getReturnDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if(rentalReturnDate.isBefore(currentDate))
        {
            showAlert("The return date is before the current date, so it cannot be extended.");
            return;
        }

        LocalDate newReturnDate = rentalReturnDate.plusDays(7); // Extend by 7 days

        selectedRental.setReturnDate(newReturnDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        DBUtils.updateRentals(selectedRental);
        rentalTableView.refresh();
        showAlert("Rental extended by 7 days");

    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void goToHomePage(ActionEvent event) {
        try {
            changeScene(event, "/Fxml/Home-page.fxml", "Welcome Back!", 804, 648,0, 0 ,0,null,null,0,null,null,null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}