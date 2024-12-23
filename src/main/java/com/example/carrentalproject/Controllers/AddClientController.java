package com.example.carrentalproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.*;

public class AddClientController {

    @FXML
    private TextField clientNameField;
    @FXML
    private TextField clientUserNameField;
    @FXML
    private TextField clientPasswordField;
    @FXML
    private TextField clientNumberField;
    @FXML
    private Label errorMessage;

    @FXML
    public void saveClient() {
        try {
            // Reset error messages
            errorMessage.setText("");

            String clientName = clientNameField.getText().trim();
            String clientUserName = clientUserNameField.getText().trim();
            String clientPassword = clientPasswordField.getText().trim();
            String clientNumber = clientNumberField.getText().trim();

            // Validate required fields
            if(clientName.isEmpty() || clientUserName.isEmpty() || clientPassword.isEmpty() || clientNumber.isEmpty()){
                errorMessage.setText("All fields are mandatory");
                return;
            }
            if(!clientNumber.matches("\\d+")){
                errorMessage.setText("Client Number must contain only numeric characters !");
                return;
            }
            insertClientIntoDB(clientName,clientUserName,clientPassword, clientNumber);
            clearForm();
        }catch (Exception e){
            errorMessage.setText("Error during client creation" + e.getMessage());

        }


    }
    private void insertClientIntoDB(String clientName,String clientUserName,String clientPassword,String clientNumber ) {

        String insertSQL = "INSERT INTO client (clientName, clientUserName, clientPassword, clientNumber) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/carrental", "root", "root");
             PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, clientName);
            preparedStatement.setString(2, clientUserName);
            preparedStatement.setString(3, clientPassword);
            preparedStatement.setString(4, clientNumber);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                errorMessage.setText("Client added successfully");

            } else {
                errorMessage.setText("Error adding the client");

            }
        }
        catch (SQLException e) {
            errorMessage.setText("SQL Error adding the new client "+ e.getMessage());

        }
    }

    private void clearForm() {
        clientNameField.clear();
        clientUserNameField.clear();
        clientPasswordField.clear();
        clientNumberField.clear();

    }
}
