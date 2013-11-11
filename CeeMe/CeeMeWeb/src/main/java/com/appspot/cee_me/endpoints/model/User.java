package com.appspot.cee_me.endpoints.model;

import com.appspot.cee_me.model.CUser;

import java.io.Serializable;

/**
 * Represents a model.CUser in the API
 */
public class User implements Serializable {
    private String key;
    private String accountName;

    public User() {
    }

    public User(String key, String accountName) {
        this.key = key;
        this.accountName = accountName;
    }

    public User(CUser cuser) {
        if (cuser != null) {
            setKey(cuser.getKey().getString());
            setAccountName(cuser.getAccountName());
        }
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
