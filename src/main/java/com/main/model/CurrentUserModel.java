package com.main.model;

import com.main.controller.CurrentUserHolder;

public class CurrentUserModel {
    private String username;
    private String firstName;
    private String lastName;
    private static String userId;
    private String password;

    public CurrentUserModel(String username, String firstName, String lastName, String userId, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.password = password;
    }

    public CurrentUserModel() {

    }

    public static void ResetUser() {
        CurrentUserModel u = new CurrentUserModel();
        CurrentUserHolder holder = CurrentUserHolder.getCurrentUser();
        holder.setCurrentUser(u);

        System.out.println(u.getFirstName() + u.getLastName() + u.getUsername() + u.getUserId());
    }

    public static void SetUserInstance(CurrentUserModel u) {
        CurrentUserHolder holder = CurrentUserHolder.getCurrentUser();
        holder.setCurrentUser(u);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public static String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
