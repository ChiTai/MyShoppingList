package com.example.myshoppinglist.myshoppinglist.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ameliebarre1 on 05/01/2017.
 */

public class User {

    private String firstname;
    private String lastname;
    private String email;
    private String token;

    public User() {}

    public User(String firstname, String lastname, String email, String token){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.token = token;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) { this.token = token; }
}
