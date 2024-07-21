package com.example.moody;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreUtil {

    public interface ApiKeyCallback {
        void onApiKeyUpdated(String apiKey);
        void onError(Exception e);
    }

    public static void updateApiKey(ApiKeyCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("API").document("1").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String apiKey = document.getString("key");
                    if (apiKey != null) {
                        callback.onApiKeyUpdated(apiKey);
                    } else {
                        callback.onError(new Exception("API key is null"));
                    }
                } else {
                    callback.onError(new Exception("Document does not exist"));
                }
            } else {
                callback.onError(task.getException());
            }
        });
    }
}
