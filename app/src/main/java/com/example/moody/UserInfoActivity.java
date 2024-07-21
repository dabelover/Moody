package com.example.moody;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.auth.FirebaseAuth;

public class UserInfoActivity extends AppCompatActivity {

    private TextView tvUserName;
    private TextView tvUserEmail;
    private EditText etApiKey;
    private Button btnSaveApiKey;
    private Button btnLogout;
    private Button btnUpdateApi;
    private UserViewModel userViewModel;
    public static String API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        etApiKey = findViewById(R.id.etApiKey);
        btnSaveApiKey = findViewById(R.id.btnSaveApiKey);
        btnLogout = findViewById(R.id.btnLogout);
        btnUpdateApi = findViewById(R.id.btnUpdateApi);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Observa los cambios en los datos del usuario
        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                tvUserName.setText(user.getName());
                tvUserEmail.setText(user.getEmail());
            }
        });

        // Acción del botón de cerrar sesión
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar sesión en Firebase
                FirebaseAuth.getInstance().signOut();

                // Redirigir a la página de login
                startActivity(new Intent(UserInfoActivity.this, Login.class));
                finish();
            }
        });

        // Acción del botón de actualizar API
        btnUpdateApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirestoreUtil.updateApiKey(new FirestoreUtil.ApiKeyCallback() {
                    @Override
                    public void onApiKeyUpdated(String apiKey) {
                        API_KEY = apiKey;
                    }

                    @Override
                    public void onError(Exception e) {
                        // Manejar error
                    }
                });
            }
        });

        // Acción del botón de guardar API Key
        btnSaveApiKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String apiKey = etApiKey.getText().toString();
                if (!apiKey.isEmpty()) {
                    API_KEY = apiKey;
                }
            }
        });
    }
}
