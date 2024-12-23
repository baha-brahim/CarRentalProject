package com.example.carrentalproject.Classes;

public class Car {
    private int carId;
    private String carBrand;
    private String carModel;
    private int carYear;
    private String carStatus;
    private String carUrl;
    private int carCostPerDat;
    private String carDesc;
    private int carNbSeats;
    private String carTransType;

    public Car(int carId, String carBrand, String carModel, int carYear, String carStatus , String carUrl, int carCostPerDat, String carDesc, int carNbSeats, String carTransType) {
        this.carId = carId;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carYear = carYear;
        this.carStatus = carStatus;
        this.carUrl = carUrl;
        this.carCostPerDat = carCostPerDat;
        this.carDesc = carDesc;
        this.carNbSeats = carNbSeats;
        this.carTransType = carTransType;
    }

    public String getCarTransType() {
        return carTransType;
    }

    public void setCarTransType(String carTransType) {
        this.carTransType = carTransType;
    }

    public int getCarNbSeats() {
        return carNbSeats;
    }

    public void setCarNbSeats(int carNbSeats) {
        this.carNbSeats = carNbSeats;
    }

    public String getCarDesc() {
        return carDesc;
    }

    public void setCarDesc(String carDesc) {
        this.carDesc = carDesc;
    }

    public int getCarCostPerDay() {
        return carCostPerDat;
    }

    public void setCarCostPerDat(int carCostPerDat) {
        this.carCostPerDat = carCostPerDat;
    }


    // GETTERS
    public int getCarId() {
        return carId;
    }
    public String getCarBrand() {
        return carBrand;
    }
    public String getCarModel() {
        return carModel;
    }
    public int getCarYear() {
        return carYear;
    }
    public String getCarStatus() {
        return carStatus;
    }
    public String getCarUrl() {
        return carUrl;
    }
}
