package com.main.a2;

public final class CurrentUserHolder {
    //This serves as a singleton class.
    //Store a single instance of a class/object and be able to access it anywhere in the program.
    // This one is used to store user details.

    private CurrentUser user;
    private final static CurrentUserHolder CURRENT_USER = new CurrentUserHolder();

    private CurrentUserHolder() {

    }

    public static CurrentUserHolder getCurrentUser() {
        return CURRENT_USER;
    }

    public void setCurrentUser(CurrentUser user) {
        this.user = user;
    }

    public CurrentUser getUser() {
        return this.user;
    }
}
