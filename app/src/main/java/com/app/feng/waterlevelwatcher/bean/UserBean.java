package com.app.feng.waterlevelwatcher.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by feng on 2017/4/13.
 */

public class UserBean extends RealmObject {
    @PrimaryKey
    private String username;

    private String password;

    private String displayName;

    public boolean autoLogin;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
