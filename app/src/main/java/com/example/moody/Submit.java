package com.example.moody;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;

public class Submit extends AppCompatActivity {

    private LinearLayout chatContainer;
    private EditText etMessage;
    private boolean firstMessage = true;
    private Button btnSend;
    private OkHttpClient client;
    private static final String API_KEY = "sk-proj-L1mjXrbel0FCjF6kxgtOT3BlbkFJqZfC3tYzqcXF913fXrvi"; // Reemplaza con tu API Key
    private static final String CHATGPT_API_URL = "https://api.openai.com/v1/chat/completions";
    private String initialMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        Intent intent = getIntent();
        initialMessage = intent.getStringExtra("message");

        chatContainer = findViewById(R.id.chatContainer);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        client = new OkHttpClient();

        // Enviar el mensaje inicial
        if (initialMessage != null && !initialMessage.isEmpty()) {
            sendMessageToChatGPT(initialMessage);
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMessage = etMessage.getText().toString();
                addMessage("You: " + userMessage, true);
                sendMessageToChatGPT(userMessage);

                etMessage.setText("");
            }
        });
    }

    private void addMessage(String message, boolean isUser) {
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setTextSize(16);
        textView.setPadding(8, 8, 8, 8);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 8, 0, 8);

        if (isUser) {
            params.gravity = Gravity.END;
            textView.setBackgroundResource(R.drawable.background_user_message);
        } else {
            params.gravity = Gravity.START;
            textView.setBackgroundResource(R.drawable.background_chatgpt_message);
        }

        textView.setLayoutParams(params);
        chatContainer.addView(textView);
    }

    private void sendMessageToChatGPT(String message) {
        JsonObject json = new JsonObject();
        json.addProperty("model", "gpt-3.5-turbo");

        JsonArray messagesArray = new JsonArray();
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", message);
        messagesArray.add(userMessage);

        json.add("messages", messagesArray);
        json.addProperty("max_tokens", 150);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json.toString()
        );

        Request request = new Request.Builder()
                .url(CHATGPT_API_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Submit", "Error en la solicitud: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                    String chatGPTResponse = jsonResponse.get("choices").getAsJsonArray()
                            .get(0).getAsJsonObject().get("message").getAsJsonObject().get("content").getAsString();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addMessage("Moody: " + chatGPTResponse, false);
                        }
                    });
                } else {
                    String responseBody = response.body().string();
                    Log.e("Submit", "Error en la respuesta: " + response.message() + "\n" + responseBody);
                }
            }
        });
    }
}
