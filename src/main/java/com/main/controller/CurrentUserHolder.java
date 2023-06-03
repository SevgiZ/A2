package com.main.controller;

import com.main.model.CurrentUserModel;

public final class CurrentUserHolder {
    //This serves as a singleton class.
    //Store a single instance of a class/object and be able to access it anywhere in the program.
    // This one is used to store user details.

    private CurrentUserModel user;
    private final static CurrentUserHolder CURRENT_USER = new CurrentUserHolder();

    private CurrentUserHolder() {

    }

    public static CurrentUserHolder getCurrentUser() {
        return CURRENT_USER;
    }

    public void setCurrentUser(CurrentUserModel user) {
        this.user = user;
    }

    public CurrentUserModel getUser() {
        return this.user;
    }
}
