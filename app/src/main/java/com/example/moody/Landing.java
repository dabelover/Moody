package com.example.moody;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Landing extends AppCompatActivity {

    Button buttonManual, buttonSync;
    ImageView imageViewSad, imageViewNeutral, imageViewHappy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        buttonManual = findViewById(R.id.buttonManual);
        buttonSync = findViewById(R.id.buttonSync);
        imageViewSad = findViewById(R.id.imageViewSad);
        imageViewNeutral = findViewById(R.id.imageViewNeutral);
        imageViewHappy = findViewById(R.id.imageViewHappy);

        // Configuración del botón manual para redirigir a MainActivity
        buttonManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Landing.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Configuración del botón de sincronización para redirigir a SamsungSincro
        buttonSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Landing.this, SamsungSincro.class);
                startActivity(intent);
            }
        });

        // Configuración de los ImageView para redirigir a ChatGptConver con emoción
        imageViewSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Landing.this, ChatGptConver.class);
                intent.putExtra("mood", "sad");
                startActivity(intent);
            }
        });

        imageViewNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Landing.this, ChatGptConver.class);
                intent.putExtra("mood", "neutral");
                startActivity(intent);
            }
        });

        imageViewHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Landing.this, ChatGptConver.class);
                intent.putExtra("mood", "happy");
                startActivity(intent);
            }
        });
    }
}
