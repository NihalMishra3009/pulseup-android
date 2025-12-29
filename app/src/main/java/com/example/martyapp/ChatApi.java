package com.example.martyapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatApi {
    @POST("send-message") // Replace with correct API endpoint
    Call<ApiResponse> sendMessage(@Body ApiRequest request);
}
