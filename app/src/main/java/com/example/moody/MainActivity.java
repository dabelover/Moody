package com.example.moody;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button buttonUser;
    private Button buttonLogout;
    private EditText editTextStepCount, editTextCalories, editTextSleepHours, editTextBoolOfActive;
    private Button buttonSubmit;
    private TextView textViewResult;
    private Interpreter tflite;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        buttonUser = findViewById(R.id.buttonUser);
        buttonLogout = findViewById(R.id.buttonLogout);
        textViewResult = findViewById(R.id.textViewResult);

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
        editTextStepCount = findViewById(R.id.editTextStepCount);
        editTextCalories = findViewById(R.id.editTextCalories);
        editTextSleepHours = findViewById(R.id.editTextSleepHours);
        editTextBoolOfActive = findViewById(R.id.editTextBoolOfActive);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Cargar el modelo de TensorFlow Lite
        try {
            tflite = new Interpreter(loadModelFile());
        } catch (IOException e) {
            Log.e("MainActivity", "Error al cargar el modelo: " + e.getMessage());
        }

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewResult.setText("Procesando...");

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        submitForm();
                    }
                }, 1000);
            }
        });
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(getAssets().openFd("modelo.tflite").getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = getAssets().openFd("modelo.tflite").getStartOffset();
        long declaredLength = getAssets().openFd("modelo.tflite").getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void modelResponse(float steps, float caloriesBurned, float hoursOfSleep, float boolOfActive, float season) {
        try {
            // Prepara el buffer de entrada
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 5); // 4 bytes por float, 5 características
            byteBuffer.order(ByteOrder.nativeOrder());
            byteBuffer.putFloat(steps);
            byteBuffer.putFloat(caloriesBurned);
            byteBuffer.putFloat(hoursOfSleep);
            byteBuffer.putFloat(boolOfActive);
            byteBuffer.putFloat(season);

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 5}, DataType.FLOAT32);
            inputFeature0.loadBuffer(byteBuffer);

            // Crear el buffer de salida
            TensorBuffer outputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 3}, DataType.FLOAT32);

            // Ejecutar la inferencia
            tflite.run(inputFeature0.getBuffer(), outputFeature0.getBuffer().rewind());

            // Manejar la salida
            float[] output = outputFeature0.getFloatArray();
            int moodIndex = argmax(output);
            String mood = interpretMood(moodIndex);

            // Mostrar el resultado
            textViewResult.setText("Predicted mood: " + mood);

        } catch (Exception e) {
            Log.e("MainActivity", "Error al ejecutar el modelo: " + e.getMessage());
        }
    }

    private int argmax(float[] array) {
        int maxIndex = -1;
        float maxValue = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private String interpretMood(int index) {
        switch (index) {
            case 0:
                return "Sad";
            case 1:
                return "Neutral";
            case 2:
                return "Happy";
            default:
                return "Unknown";
        }
    }

    private void submitForm() {
        // Capturar los valores ingresados
        float steps = Float.parseFloat(editTextStepCount.getText().toString().trim());
        float sleepHours = Float.parseFloat(editTextSleepHours.getText().toString().trim());
        float calories = Float.parseFloat(editTextCalories.getText().toString().trim());
        float boolOfActive = Float.parseFloat(editTextBoolOfActive.getText().toString().trim());

        // Obtener la fecha actual
        Date date = new Date();

        // Determinar la estación del año
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        float season;
        if (month == Calendar.DECEMBER || month == Calendar.JANUARY || month == Calendar.FEBRUARY) {
            season = 2; // Invierno
        } else if (month >= Calendar.MARCH && month <= Calendar.MAY) {
            season = 3; // Primavera
        } else if (month >= Calendar.JUNE && month <= Calendar.AUGUST) {
            season = 4; // Verano
        } else {
            season = 1; // Otoño
        }

        // Llamar a la función que procesa el modelo
        modelResponse(steps, calories, sleepHours, boolOfActive, season);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tflite != null) {
            tflite.close();
        }
    }
}
