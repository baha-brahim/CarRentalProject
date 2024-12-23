package com.example.carrentalproject.Classes;

public class Rental {
    private int rentalId;
    private int clientId;
    private int carId;
    private String carModel;
    private String rentalDate;
    private String returnDate;
    private double totalCost;
    private String pickUpLocation;
    private String rentalState;

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public Rental(String carModel, String rentalDate, String returnDate, double totalCost, String pickUpLocation, String rentalState) {
        this.carModel = carModel;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.totalCost = totalCost;
        this.pickUpLocation = pickUpLocation;
        this.rentalState = rentalState;
    }

    public Rental(int rentalId, int clientId, int carId, String rentalDate, String returnDate, double totalCost, String pickUpLocation, String rentalState) {
        this.rentalId = rentalId;
        this.clientId = clientId;
        this.carId = carId;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.totalCost = totalCost;
        this.pickUpLocation = pickUpLocation;
        this.rentalState = rentalState;
    }

    public int getRentalId() {
        return rentalId;
    }

    public String getRentalState() {
        return rentalState;
    }

    public void setRentalState(String rentalState) {
        this.rentalState = rentalState;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(String rentalDate) {
        this.rentalDate = rentalDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }
}
