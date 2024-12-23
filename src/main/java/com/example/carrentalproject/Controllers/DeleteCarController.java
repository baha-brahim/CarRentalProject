package com.example.carrentalproject.Controllers;


import com.example.carrentalproject.Classes.Car;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.sql.*;
public class DeleteCarController {

    @FXML
    private TableView<Car> carTable;
    @FXML
    private TableColumn<Car, Integer> carIdColumn;
    @FXML
    private TableColumn<Car, String> carModelColumn;
    @FXML
    private TableColumn<Car, String> carBrandColumn;
    @FXML
    private TableColumn<Car, Integer> carYearColumn;
    @FXML
    private TableColumn<Car, String> carUrlColumn;
    @FXML
    private TableColumn<Car, Double> carCostColumn;
    @FXML
    private TableColumn<Car, String> carDescColumn;
    @FXML
    private TableColumn<Car, Integer> carNbSeatsColumn;
    @FXML
    private TableColumn<Car, String> carTransTypeColumn;
    @FXML
    private TableColumn<Car, Void> deleteCarColumn;

    @FXML
    private Label errorMessage;

    private static final String JDBC_URL = "jdbc:sqlite:your_database.db";
    @FXML
    public void initialize() {
        loadCarsData();
    }
    private void loadCarsData() {
        ObservableList<Car> carList = FXCollections.observableArrayList();
        String selectSQL = "SELECT * FROM car";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/carrental", "root", "root");
             PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
             ResultSet resultSet = preparedStatement.executeQuery())
        {
            while (resultSet.next()) {
                int carId = resultSet.getInt("carId");
                String carModel = resultSet.getString("carModel");
                String carBrand = resultSet.getString("carBrand");
                int carYear = resultSet.getInt("carYear");
                String carUrl = resultSet.getString("carUrl");
                double carCost = resultSet.getDouble("carCostPerDay");
                String carDesc = resultSet.getString("carDesc");
                int carNbSeats = resultSet.getInt("carNbSeats");
                String carTransType = resultSet.getString("carTransType");

                Car car = new Car(carId, carModel, carBrand, carYear, "available", carUrl, (int) carCost,carDesc , carNbSeats,carTransType);
                carList.add(car);
            }
            carIdColumn.setCellValueFactory(new PropertyValueFactory<>("carId"));
            carModelColumn.setCellValueFactory(new PropertyValueFactory<>("carModel"));
            carBrandColumn.setCellValueFactory(new PropertyValueFactory<>("carBrand"));
            carYearColumn.setCellValueFactory(new PropertyValueFactory<>("carYear"));
            carUrlColumn.setCellValueFactory(new PropertyValueFactory<>("carUrl"));
            carCostColumn.setCellValueFactory(new PropertyValueFactory<>("carCostPerDay"));
            carDescColumn.setCellValueFactory(new PropertyValueFactory<>("carDesc"));
            carNbSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("carNbSeats"));
            carTransTypeColumn.setCellValueFactory(new PropertyValueFactory<>("carTransType"));

            // Delete button configuration
            deleteCarColumn.setCellFactory(param -> new TableCell<>() {
                private final Button deleteButton = new Button("Delete");
                {
                    deleteButton.getStyleClass().add("btn");
                    deleteButton.setOnAction(event -> {
                        Car car = getTableRow().getItem();
                        if (car != null) {
                            deleteCarFromDB(car.getCarId());
                            loadCarsData();// Refresh table
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty){
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }

                }
            });

            carTable.setItems(carList);
        } catch (SQLException e) {
            errorMessage.setText("SQL error loading cars " +e.getMessage());

        }
    }

    private void deleteCarFromDB(int carId) {
        String deleteSQL = "DELETE FROM car WHERE carId = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/carrental", "root", "root");
             PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, carId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                errorMessage.setText("Car deleted.");
            } else {
                errorMessage.setText("Car could not be deleted.");

            }

        }
        catch (SQLException e) {
            errorMessage.setText("SQL Error deleting car" + e.getMessage());
        }
    }


}