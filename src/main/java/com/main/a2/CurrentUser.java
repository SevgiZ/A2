package com.main.a2;

import java.util.concurrent.CopyOnWriteArrayList;

public class CurrentUser {
    private String username;
    private String firstName;
    private String lastName;
    private String userId;

    public CurrentUser(String username, String firstName, String lastName, String userId) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
    }

    public CurrentUser() {

    }

    public static void ResetUser() {
        CurrentUser u = new CurrentUser();
        CurrentUserHolder holder = CurrentUserHolder.getCurrentUser();
        holder.setCurrentUser(u);

        System.out.println(u.getFirstName() + u.getLastName() + u.getUsername() + u.getUserId());
    }

    public static void SetUserInstance(CurrentUser u) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
