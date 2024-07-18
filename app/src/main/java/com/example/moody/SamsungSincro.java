package com.example.moody;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SamsungSincro extends AppCompatActivity {

    Button buttonManual, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samsung_sincro);

        buttonManual = findViewById(R.id.buttonManual);
        buttonBack = findViewById(R.id.buttonSync); // Asegúrate de cambiar el ID si es necesario

        // Configuración del botón para introducir datos manualmente
        buttonManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SamsungSincro.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finaliza la actividad actual
            }
        });

        // Configuración del botón para volver
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finaliza la actividad actual
            }
        });
    }
}
