package com.example.moody;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Submit extends AppCompatActivity {

    private TextView responseTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit); // Asegúrate de que este es el layout correcto

        responseTextView = findViewById(R.id.responseTextView);

        Intent intent = getIntent();
        String message = intent.getStringExtra("message");

        GeminiService geminiService = new GeminiService();
        geminiService.sendPrompt(message, new GeminiService.GeminiCallback() {
            @Override
            public void onResponse(String response) {
                runOnUiThread(() -> responseTextView.setText(response));
            }

            @Override
            public void onFailure(Throwable t) {
                runOnUiThread(() -> responseTextView.setText("Error: " + t.getMessage()));
            }
        });
    }
}
