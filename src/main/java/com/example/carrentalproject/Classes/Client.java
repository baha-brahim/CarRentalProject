package com.example.carrentalproject.Classes;

// Client Model
public class Client {
    private int clientId;
    private String clientName;
    private String clientUserName;
    private String clientPassword;
    private String clientNumber;
    public Client(int clientId,String clientName,String clientUserName, String clientPassword,String clientNumber){
        this.clientId=clientId;
        this.clientName =clientName;
        this.clientUserName=clientUserName;
        this.clientPassword=clientPassword;
        this.clientNumber=clientNumber;

    }
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientUserName() {
        return clientUserName;
    }

    public void setClientUserName(String clientUserName) {
        this.clientUserName = clientUserName;
    }

    public String getClientPassword() {
        return clientPassword;
    }

    public void setClientPassword(String clientPassword) {
        this.clientPassword = clientPassword;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }
}