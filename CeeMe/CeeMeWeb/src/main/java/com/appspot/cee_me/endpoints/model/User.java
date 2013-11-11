package com.appspot.cee_me.endpoints.model;

import com.appspot.cee_me.model.CUser;

import java.io.Serializable;

/**
 * Represents a model.CUser in the API
 */
public class User implements Serializable {
    private String userKey;
    private String accountName;

    public User() {
    }

    public User(String userKey, String accountName) {
        this.userKey = userKey;
        this.accountName = accountName;
    }

    public User(CUser cuser) {
        if (cuser != null) {
            setUserKey(cuser.getKey().getString());
            setAccountName(cuser.getAccountName());
        }
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
