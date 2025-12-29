package com.example.martyapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GeminiApi {
    @POST("{modelPath}")
    Call<GeminiResponse> generateContent(
            @Path(value = "modelPath", encoded = true) String modelPath,
            @Body GeminiRequest request
    );
}
