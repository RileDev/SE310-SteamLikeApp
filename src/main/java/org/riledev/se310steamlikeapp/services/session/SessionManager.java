package org.riledev.se310steamlikeapp.services.session;

import org.riledev.se310steamlikeapp.models.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager(){}

    public static synchronized SessionManager getInstance(){
        if(instance == null){
            instance = new SessionManager();
        }

        return instance;
    }

    public void loginUser(User user){
        this.currentUser = user;
        System.out.println("Session started for user: " + user.getUsername());
    }

    public void logoutUser(){
        System.out.println("Session ended for user: " + (currentUser != null ? currentUser.getUsername() : "Unknown"));
        this.currentUser = null;
    }

    public User getCurrentUser(){
        return this.getCurrentUser();
    }

    public boolean isUserLoggedIn(){
        return this.currentUser != null;
    }

}
