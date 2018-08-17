package com.example.deepakkatiyar.loginpage;

import com.google.firebase.database.IgnoreExtraProperties;

public class user {
    public String name;
    public String email;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public user() {
    }

    public user(String name, String email) {
        this.name = name;
        this.email = email;
    }
}

