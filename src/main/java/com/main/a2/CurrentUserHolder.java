package com.main.a2;

public final class CurrentUserHolder {

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
