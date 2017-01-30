package com.example.myshoppinglist.myshoppinglist.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ameliebarre1 on 05/01/2017.
 */

public class ShoppingList {

    public int id;
    public String name;
    public String created_date;
    public boolean completed;

    public ShoppingList(int id, String name, String created_date, boolean completed){
        this.id = id;
        this.name = name;
        this.created_date = created_date;
        this.completed = completed;
    }

    public ShoppingList(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}
