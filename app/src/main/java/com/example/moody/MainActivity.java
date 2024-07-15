package com.example.moody;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText editTextSteps, editTextCaloriesBurned, editTextDistanceKm, editTextActiveMinutes,
            editTextSleepHours, editTextHeartRateAvg, editTextWorkoutType, editTextWeatherConditions, editTextLocation;
    private Button buttonSubmit;

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

        // Inicializar los campos de entrada
        editTextSteps = findViewById(R.id.editTextSteps);
        editTextCaloriesBurned = findViewById(R.id.editTextCaloriesBurned);
        editTextDistanceKm = findViewById(R.id.editTextDistanceKm);
        editTextActiveMinutes = findViewById(R.id.editTextActiveMinutes);
        editTextSleepHours = findViewById(R.id.editTextSleepHours);
        editTextHeartRateAvg = findViewById(R.id.editTextHeartRateAvg);
        editTextWorkoutType = findViewById(R.id.editTextWorkoutType);
        editTextWeatherConditions = findViewById(R.id.editTextWeatherConditions);
        editTextLocation = findViewById(R.id.editTextLocation);

        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }

    private void submitForm() {
        // Capturar los valores ingresados
        String steps = editTextSteps.getText().toString().trim();
        String caloriesBurned = editTextCaloriesBurned.getText().toString().trim();
        String distanceKm = editTextDistanceKm.getText().toString().trim();
        String activeMinutes = editTextActiveMinutes.getText().toString().trim();
        String sleepHours = editTextSleepHours.getText().toString().trim();
        String heartRateAvg = editTextHeartRateAvg.getText().toString().trim();
        String workoutType = editTextWorkoutType.getText().toString().trim();
        String weatherConditions = editTextWeatherConditions.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();

        // Aquí puedes manejar los datos como desees, por ejemplo, enviarlos a una API o guardarlos en una base de datos.
        // Para este ejemplo, simplemente vamos a mostrar un Toast con los datos capturados.

        String message = "Actúa como coach motivacional, sabiendo como ha ido mi día: " +
                "steps: " + steps + " " +
                "calories_burned: " + caloriesBurned + " " +
                "distance_km: " + distanceKm + " " +
                "active_minutes: " + activeMinutes + " " +
                "sleep_hours: " + sleepHours + " " +
                "heart_rate: " + heartRateAvg + " " +
                "workout_type: " + workoutType + " " +
                "weather_condition: " + weatherConditions + " " +
                "location: " + location + " " +
                "mood: Tired " + //Recordar implementar el mood
                "Hazme una pregunta para reconducir mi ánimo hacia estar de mejor humor. Solo puedes hacer una pregunta, elije la más adecuada. Empieza la respuesta resaltando mi estado de ánimo con el fin de mejorarlo si es triste";

        Log.d("MainActivity", message);

        Intent intent = new Intent(MainActivity.this, Submit.class);
        intent.putExtra("message", message);
        startActivity(intent);

    }
}