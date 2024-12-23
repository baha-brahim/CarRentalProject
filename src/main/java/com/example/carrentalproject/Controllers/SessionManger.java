package com.example.carrentalproject.Controllers;

public class SessionManger {
    public static int currentUserId;
    public static String currentUserName;

    public static String getCurrentUserName() {
        return currentUserName;
    }

    public static void setCurrentUserName(String currentUserName) {
        SessionManger.currentUserName = currentUserName;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static void setCurrentUserId(int currentUserId) {
        SessionManger.currentUserId = currentUserId;
    }
}
