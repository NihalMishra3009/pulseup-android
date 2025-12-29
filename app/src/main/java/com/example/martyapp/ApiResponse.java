package com.example.martyapp;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {
    @SerializedName("reply") // Check actual JSON response key
    private String response;

    public String getResponse() {
        return response;
    }
}
