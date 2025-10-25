package com.example.insta;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.insta.managers.UserManager;
import com.example.insta.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etEmail, etPassword, etFullName;
    private Button btnRegister;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userManager = new UserManager(this);
        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etFullName = findViewById(R.id.etFullName);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        User newUser = new User(
                String.valueOf(System.currentTimeMillis()), // Генерируем ID
                username,
                email,
                fullName
        );

        if (userManager.registerUser(newUser)) {
            userManager.setCurrentUser(newUser);
            Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Пользователь с таким именем или email уже существует", Toast.LENGTH_SHORT).show();
        }
    }
}