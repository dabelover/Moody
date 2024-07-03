package com.example.moody;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button buttonUser;
    private Button buttonLogout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        buttonUser = findViewById(R.id.buttonUser);

        buttonLogout = findViewById(R.id.buttonLogout);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            buttonUser.setText(email);
        } else {
            // Si no hay usuario logueado, redirigir a Login
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
                Toast.makeText(MainActivity.this, "Logged out successfully.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

