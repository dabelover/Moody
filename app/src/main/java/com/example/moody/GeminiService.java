package com.example.moody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GeminiService {

    private GeminiApi geminiApi;

    public interface GeminiCallback {
        void onResponse(String response);
        void onFailure(Throwable t);
    }

    public GeminiService() {
        Retrofit retrofit = RetrofitClient.getClient("https://api.gemini.com/"); // Reemplaza con la URL base correcta
        geminiApi = retrofit.create(GeminiApi.class);
    }

    public void sendPrompt(String prompt, final GeminiCallback callback) {
        GeminiRequest request = new GeminiRequest(prompt);
        Call<GeminiResponse> call = geminiApi.getResponse(request);

        call.enqueue(new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResponse(response.body().getText());
                } else {
                    callback.onFailure(new Exception("Request failed with code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
