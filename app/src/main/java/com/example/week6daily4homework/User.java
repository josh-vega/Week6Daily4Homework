package com.example.week6daily4homework;

import java.security.KeyPair;

public class User {
    private String username;
    private String password;
    private KeyPair keypair;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public KeyPair getKeypair() {
        return keypair;
    }
}
