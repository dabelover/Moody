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
    String message;

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
                message = "Actúa como coach motivacional\nMi estado de ánimo es triste. Dame un consejo sin hacer alusión a que te he preguntado. Debe ser un consejo corto, no te extiendas mucho. ";

                Intent intent = new Intent(Landing.this, Submit.class);
                intent.putExtra("message", message);
                startActivity(intent);
            }
        });

        imageViewNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = "Actúa como coach motivacional\nMi estado de ánimo es neutral. Dame un consejo sin hacer alusión a que te he preguntado. Debe ser un consejo corto, no te extiendas mucho. ";

                Intent intent = new Intent(Landing.this, Submit.class);
                intent.putExtra("message", message);
                startActivity(intent);
            }
        });

        imageViewHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = "Actúa como coach motivacional\nMi estado de ánimo es feliz. Dame un consejo sin hacer alusión a que te he preguntado. Debe ser un consejo corto, no te extiendas mucho. ";

                Intent intent = new Intent(Landing.this, Submit.class);
                intent.putExtra("message", message);
                startActivity(intent);
            }
        });
    }
}
