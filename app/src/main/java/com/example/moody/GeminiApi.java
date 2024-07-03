package com.example.moody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GeminiApi {

    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer AIzaSyBGKB-zJNzUJwUyK18-VUc743xpZE5pQlw" // Reemplaza YOUR_API_KEY con tu API Key
    })
    @POST("path/to/your/endpoint") // Reemplaza con el endpoint correcto de la API de Gemini
    Call<GeminiResponse> getResponse(@Body GeminiRequest request);
}

