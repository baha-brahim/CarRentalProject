package com.example.carrentalproject.Controllers;

import com.example.carrentalproject.Classes.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;

public class DeleteClientController {

    @FXML
    private TableView<Client> clientTable;
    @FXML
    private TableColumn<Client, Integer> clientIdColumn;
    @FXML
    private TableColumn<Client, String> clientNameColumn;
    @FXML
    private TableColumn<Client, String> clientUserNameColumn;
    @FXML
    private TableColumn<Client, String> clientPasswordColumn;
    @FXML
    private TableColumn<Client, String> clientNumberColumn;
    @FXML
    private TableColumn<Client,Void> deleteClientColumn;
    @FXML
    private Label errorMessage;


    @FXML
    public void initialize() {
        loadClientsData();
    }

    private void loadClientsData() {
        ObservableList<Client> clientList = FXCollections.observableArrayList();
        String selectSQL = "SELECT * FROM client";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/carrental", "root", "root");
             PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
             ResultSet resultSet = preparedStatement.executeQuery())
        {
            while (resultSet.next()) {
                int clientId = resultSet.getInt("clientId");
                String clientName = resultSet.getString("clientName");
                String clientUserName = resultSet.getString("clientUserName");
                String clientPassword = resultSet.getString("clientPassword");
                String clientNumber = resultSet.getString("clientNumber");

                Client client = new Client(clientId,clientName,clientUserName,clientPassword,clientNumber);
                clientList.add(client);
            }

            clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));
            clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
            clientUserNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientUserName"));
            clientPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("clientPassword"));
            clientNumberColumn.setCellValueFactory(new PropertyValueFactory<>("clientNumber"));


            deleteClientColumn.setCellFactory(param -> new TableCell<>() {
                private final Button deleteButton = new Button("Delete");
                {
                    deleteButton.getStyleClass().add("btn");
                    deleteButton.setOnAction(event -> {
                        Client client = getTableRow().getItem();
                        if (client != null) {
                            deleteClientFromDB(client.getClientId());
                            loadClientsData();// Refresh the table
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty){
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            });

            clientTable.setItems(clientList);

        } catch (SQLException e) {
            errorMessage.setText("SQL error loading clients: " +e.getMessage());

        }
    }

    private void deleteClientFromDB(int clientId) {
        String deleteSQL = "DELETE FROM client WHERE clientId = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/carrental", "root", "root");
             PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, clientId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                errorMessage.setText("Client deleted.");
            } else {
                errorMessage.setText("Client not found or could not be deleted");

            }
        }
        catch (SQLException e) {
            errorMessage.setText("SQL Error Deleting client " + e.getMessage());

        }
    }
}