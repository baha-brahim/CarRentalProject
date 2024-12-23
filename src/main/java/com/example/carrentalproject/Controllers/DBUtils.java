package com.example.carrentalproject.Controllers;

import com.example.carrentalproject.Classes.Car;
import com.example.carrentalproject.Classes.Rental;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.animation.FadeTransition;
import javafx.scene.layout.Pane;


public class DBUtils {
    private int retrievedClientId;

    // Modified changeScene method
    public static void changeScene(ActionEvent event, String fxmlFile, String title, int x, int y,
                                   int clientId, int carId, int carCostPerDay,
                                   String carUrl, String carBrand, int carNbSeats,
                                   String carModel, String carTransType, String carDesc) throws IOException {
        FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Create fade-out effect for the current scene
        Scene currentScene = stage.getScene();
        Pane currentRoot = (Pane) currentScene.getRoot();
        FadeTransition fadeOut = new FadeTransition(Duration.millis(250), currentRoot);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        // After fade-out, set the new scene and apply fade-in
        fadeOut.setOnFinished(e -> {
            stage.setScene(new Scene(root, x, y));
            stage.setTitle(title);

            // Create fade-in effect for the new scene
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();

        // Pass data to the new controller, if applicable
        if (carNbSeats != 0) {
            RegistrationFormController registrationFormController = loader.getController();
            registrationFormController.setChosenCarData(clientId, carId, carCostPerDay, carUrl, carBrand, carNbSeats, carModel, carTransType, carDesc);
        }
    }


    public static void signUpUser(ActionEvent event, String name, String username, String password, String number) {
        Connection connection = null;
        PreparedStatement psCheckUserExist = null;
        PreparedStatement psInsert = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CarRental", "root", "root");
            psCheckUserExist = connection.prepareStatement("SELECT * FROM client WHERE clientUserName = ?");
            psCheckUserExist.setString(1, username);
            resultSet = psCheckUserExist.executeQuery();

            if (resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("User already exists");
                alert.showAndWait();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO client(clientName,clientUserName,clientPassword,clientNumber) VALUES(?,?,?,?)");
                psInsert.setString(1, name);
                psInsert.setString(2, username);
                psInsert.setString(3, password);
                psInsert.setString(4, number);
                psInsert.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("User successfully added");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckUserExist != null) {
                try {
                    psCheckUserExist.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void logInUser(ActionEvent event, String username, String password) throws IOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (Objects.equals(username, "admin") && Objects.equals(password, "admin")) {
            changeScene(event, "/Fxml/admin-home-page.fxml", "Welcome Back!", 1200, 700, 0, 0, 0, null, null, 0, null, null, null);
            SessionManger.setCurrentUserName("admin");
            return;
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carrental", "root", "root");
            preparedStatement = connection.prepareStatement("SELECT clientId ,clientPassword,clientUserName FROM client WHERE clientUserName = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Wrong username or password");
                alert.showAndWait();
            } else {
                resultSet.next();
                String retrievedPassword = resultSet.getString("clientPassword");
                int retrievedClientId = resultSet.getInt("clientId");
                String retrievedClientUserName = resultSet.getString("clientUserName");
                if (retrievedClientUserName == "admin" && retrievedPassword == "admin") {
                    return ;
                }
                SessionManger.setCurrentUserId(retrievedClientId);
                if (retrievedPassword.equals(password)) {
                    changeScene(event, "/Fxml/Home-page.fxml", "Welcome Back!", 804, 648,0, 0 ,0 ,null,null,0,null,null,null);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Wrong password");
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void populateCarsFromDB(AnchorPane carContainer) {
        // Create and configure the ScrollPane
        ScrollPane scrollPane = createScrollPane();

        // Create and configure the HBox for card layout
        HBox cardContainer = createCardContainer();

        // Fetch cars from the database and add cards to the container
        List<Car> cars = getCarsFromDatabase();
        cars.forEach(car -> cardContainer.getChildren().add(createCarCard(car)));

        // Add the HBox to the ScrollPane
        scrollPane.setContent(cardContainer);

        // Create scroll buttons
        Button leftButton = createScrollButton("◀");
        Button rightButton = createScrollButton("▶");

        // Position buttons inside the ScrollPane
        AnchorPane.setLeftAnchor(leftButton, 10.0);
        AnchorPane.setTopAnchor(leftButton, 150.0);
        AnchorPane.setRightAnchor(rightButton, 10.0);
        AnchorPane.setTopAnchor(rightButton, 150.0);

        // Add enhanced scroll button functionality with animation
        addAnimatedScrollFunctionality(scrollPane, leftButton, rightButton, cardContainer);

        // Position the ScrollPane within the AnchorPane
        AnchorPane.setTopAnchor(scrollPane, 10.0);
        AnchorPane.setLeftAnchor(scrollPane, 10.0);
        AnchorPane.setRightAnchor(scrollPane, 10.0);

        // Add the ScrollPane to the AnchorPane
        carContainer.getChildren().addAll(scrollPane , leftButton, rightButton);
    }

    private static void addAnimatedScrollFunctionality(ScrollPane scrollPane, Button leftButton, Button rightButton, HBox cardContainer) {
        Timeline scrollAnimation = new Timeline();
        scrollAnimation.setCycleCount(1);

        // Duration for the animation (adjust as needed)
        Duration scrollDuration = Duration.millis(500);

        // Show or hide buttons based on scroll position
        scrollPane.hvalueProperty().addListener((obs, oldVal, newVal) -> {
            leftButton.setVisible(newVal.doubleValue() > 0);
            rightButton.setVisible(newVal.doubleValue() < 1);
        });

        // Calculate scroll amount based on container width
        double scrollAmount = 700.0; // Pixels to scroll

        leftButton.setOnAction(e -> {
            // Stop any ongoing animation
            scrollAnimation.stop();

            // Calculate new scroll value
            double currentValue = scrollPane.getHvalue();
            double targetValue = Math.max(0, currentValue - (scrollAmount / cardContainer.getWidth()));

            // Create new animation
            KeyValue keyValue = new KeyValue(scrollPane.hvalueProperty(), targetValue, Interpolator.EASE_BOTH);
            KeyFrame keyFrame = new KeyFrame(scrollDuration, keyValue);

            scrollAnimation.getKeyFrames().clear();
            scrollAnimation.getKeyFrames().add(keyFrame);
            scrollAnimation.play();
        });

        rightButton.setOnAction(e -> {
            // Stop any ongoing animation
            scrollAnimation.stop();

            // Calculate new scroll value
            double currentValue = scrollPane.getHvalue();
            double targetValue = Math.min(1, currentValue + (scrollAmount / cardContainer.getWidth()));

            // Create new animation
            KeyValue keyValue = new KeyValue(scrollPane.hvalueProperty(), targetValue, Interpolator.EASE_BOTH);
            KeyFrame keyFrame = new KeyFrame(scrollDuration, keyValue);

            scrollAnimation.getKeyFrames().clear();
            scrollAnimation.getKeyFrames().add(keyFrame);
            scrollAnimation.play();
        });

        // Make buttons visible when mouse enters the scroll pane
        scrollPane.setOnMouseEntered(e -> {
            if (scrollPane.getHvalue() > 0) {
                leftButton.setVisible(true);
            }
            if (scrollPane.getHvalue() < 1) {
                rightButton.setVisible(true);
            }
        });

        // Hide buttons when mouse exits the scroll pane
        scrollPane.setOnMouseExited(e -> {
            if (!scrollPane.isPressed()) {
                leftButton.setVisible(false);
                rightButton.setVisible(false);
            }
        });
    }

    private static Button createScrollButton(String text) {
        Button button = new Button(text);
        button.setStyle("""
        -fx-background-color: #5c95ff;
        -fx-text-fill: white;
        -fx-font-size: 16px;
        -fx-border-radius: 50%;
        -fx-background-radius: 50%;
        -fx-min-width: 40px;
        -fx-min-height: 40px;
        -fx-max-width: 40px;
        -fx-max-height: 40px;
        -fx-cursor: hand;
        """);

        return button;
    }

    private static ScrollPane createScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        scrollPane.setPannable(false);
        scrollPane.setPrefHeight(350);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        return scrollPane;
    }

    private static HBox createCardContainer() {
        HBox cardContainer = new HBox(20);
        cardContainer.setPadding(new Insets(10));
        cardContainer.setAlignment(Pos.CENTER_LEFT);
        return cardContainer;
    }

    private static VBox createCarCard(Car car) {
        VBox card = new VBox(0);
        card.setStyle("-fx-border-color: #5c95ff; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefSize(200, 300);

        // Add car's image
        ImageView carImage = new ImageView(new Image(car.getCarUrl()));
        carImage.setFitWidth(180);
        carImage.setFitHeight(120);
        carImage.setPreserveRatio(true);

        // Add car's brand
        Label carBrand = new Label(car.getCarBrand());
        carBrand.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 0 0 0;");

        // Add car's model
        Label carModel = new Label(car.getCarModel());
        carModel.setStyle("-fx-font-size: 12px;");

        // Add empty region for spacing
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        RegistrationFormController registrationFormController = new RegistrationFormController();
        // Add Buy button
        Button buyButton = createBuyButton(car);
        buyButton.setOnAction(event -> {
            try {
                changeScene(event ,"/Fxml/rent-car.fxml" ,"Rent", 617, 256, SessionManger.getCurrentUserId(),car.getCarId(), car.getCarCostPerDay(), car.getCarUrl(),car.getCarBrand(),car.getCarNbSeats(),car.getCarModel(),car.getCarTransType(),car.getCarDesc());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Add all elements to the card
        card.getChildren().addAll(carImage, carBrand, carModel, spacer, buyButton);

        return card;
    }

    private static Button createBuyButton(Car car) {
        Button buyButton = new Button(car.getCarCostPerDay() + " TND " + "Rent");

        buyButton.setStyle("""
        -fx-background-color: #5c95ff;
        -fx-text-fill: white;
        -fx-border-color: grey;
        -fx-border-width: 1px 0 0 0;
        -fx-border-radius: 0 0 10px 10px;
        -fx-background-radius: 0 0 10px 10px;
        -fx-padding: 5 0 5 0;
        """);
        buyButton.setPrefWidth(200);
        return buyButton;
    }


    public static List<Car> getCarsFromDatabase() {
        List<Car> cars = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carrental", "root", "root");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT carId, carBrand, carModel, carYear, carStatus, carUrl, carCostPerDay, carDesc, carNbSeats, carTransType FROM car WHERE carStatus='F'");

            while (resultSet.next()) {
                int carId = resultSet.getInt("carId");
                String carBrand = resultSet.getString("carBrand");
                String carModel = resultSet.getString("carModel");
                int carYear = resultSet.getInt("carYear");
                String carStatus = resultSet.getString("carStatus");
                String carUrl = resultSet.getString("carUrl");
                String carDesc = resultSet.getString("carDesc");
                int carNbSeats = resultSet.getInt("carNbSeats");
                String carTransType = resultSet.getString("carTransType");
                int carCostPerDay = resultSet.getInt("carCostPerDay");

                cars.add(new Car(carId, carBrand, carModel, carYear, carStatus, carUrl,carCostPerDay, carDesc, carNbSeats, carTransType));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cars;
    }

    public static void fillRental(int carId , int clientId , LocalDate rentalDate ,LocalDate returnDate ,int totalCost ,String pickUpLocation) {
        {
            Connection connection = null;
            PreparedStatement psCheckUserExist = null;
            PreparedStatement psInsert = null;
            PreparedStatement psInsert2 = null;
            ResultSet resultSet = null;

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CarRental", "root", "root");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                psInsert2 = connection.prepareStatement("UPDATE car SET carStatus = 'B' WHERE carId=?");
                psInsert2.setInt(1, carId);
                psInsert = connection.prepareStatement("INSERT INTO rental(carId ,clientId ,rentalDate,returnDate ,totalCost ,pickUpLocation ,rentalState) VALUES(?,?,?,?,?,?,?)");
                psInsert.setString(1, carId+"");
                psInsert.setString(2, clientId+"");
                psInsert.setString(3, rentalDate.format(formatter));
                psInsert.setString(4, returnDate.format(formatter));
                psInsert.setString(5, totalCost+"");
                psInsert.setString(6, pickUpLocation);
                psInsert.setString(7, "On going");
                psInsert.executeUpdate();
                psInsert2.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Enjoy you're ride");
                alert.showAndWait();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (psCheckUserExist != null) {
                    try {
                        psCheckUserExist.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (psInsert != null) {
                    try {
                        psInsert.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void updateRentals(Rental rental) {
        String getCarIdQuery = "SELECT carId FROM car WHERE carModel = ?";
        String updateRentalQuery;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CarRental", "root", "root");
             PreparedStatement getCarIdStatement = connection.prepareStatement(getCarIdQuery)) {

            getCarIdStatement.setString(1, rental.getCarModel());

            try (ResultSet resultSet = getCarIdStatement.executeQuery()) {
                if (resultSet.next()) {
                    int carId = resultSet.getInt("carId");

                    if(rental.getReturnDate() != null)
                    {
                        updateRentalQuery = "UPDATE rental SET returnDate = ? WHERE carId = ?";
                        try (PreparedStatement updateRentalStatement = connection.prepareStatement(updateRentalQuery)) {
                            updateRentalStatement.setString(1, rental.getReturnDate());
                            updateRentalStatement.setInt(2, carId);
                            updateRentalStatement.executeUpdate();
                            System.out.println("return date extended in the db");
                        }
                    }
                    if(rental.getRentalState() != null)
                    {
                        updateRentalQuery = "UPDATE rental SET rentalState = ? WHERE carId = ?";
                        try (PreparedStatement updateRentalStatement = connection.prepareStatement(updateRentalQuery)) {
                            updateRentalStatement.setString(1, "Canceled");
                            updateRentalStatement.setInt(2, carId);
                            updateRentalStatement.executeUpdate();
                            System.out.println("rental state changed in the db");
                        }
                    }

                } else {
                    System.out.println("Car not found for model: " + rental.getCarModel()); // Log or handle missing car
                }
            }

        } catch (SQLException e) {
            System.out.println(e); // Log or handle the exception
        }
    }
}