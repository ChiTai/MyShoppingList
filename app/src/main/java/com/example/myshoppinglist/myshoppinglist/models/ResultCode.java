package com.example.myshoppinglist.myshoppinglist.models;

/**
 * Created by ameliebarre1 on 05/01/2017.
 */

public enum ResultCode {

    SUCCESS(0, "OK"),
    REQUIRED_PARAMETERS(1, "Missing required parameter(s)"),
    EMAIL_ERROR(2, "Email already registered"),
    LOGIN_ERROR(3, "Login failed check your credentials"),
    INVALID_TOKEN(4, "Invalid token"),
    SERVER_ERROR(5, "Internal server error"),
    UNAUTHORIZED_ACTION(6, "Unauthorized action"),
    NO_UPDATE(7, "Nothing to update");

    private final int code;
    private final String description;

    private ResultCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
