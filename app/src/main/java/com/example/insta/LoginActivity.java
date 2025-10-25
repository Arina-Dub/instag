package com.example.insta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.insta.managers.UserManager;
import com.example.insta.User;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userManager = new UserManager(this);
        initViews();
        setupClickListeners();
        createTestUsers();

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userManager.loginUser(username, password)) {
            Toast.makeText(this, "Добро пожаловать, " + username + "!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Неверные данные", Toast.LENGTH_SHORT).show();
        }
    }

    private void createTestUsers() {
        List<User> existingUsers = userManager.getAllUsers();
        if (existingUsers.isEmpty()) {
            User user1 = new User("1", "alex", "alex@example.com", "Алексей Иванов");
            user1.setBio("Люблю фотографировать природу и путешествовать");
            user1.setFollowersCount(150);
            user1.setFollowingCount(89);

            User user2 = new User("2", "maria", "maria@example.com", "Мария Петрова");
            user2.setBio("Фотограф | Блогер | Любитель кофе");
            user2.setFollowersCount(289);
            user2.setFollowingCount(156);

            User user3 = new User("3", "demo", "demo@example.com", "Демо Пользователь");
            user3.setBio("Это демо аккаунт для тестирования приложения");
            user3.setFollowersCount(42);
            user3.setFollowingCount(31);

            userManager.registerUser(user1);
            userManager.registerUser(user2);
            userManager.registerUser(user3);

            Toast.makeText(this, "Создано 3 тестовых пользователя: alex, maria, demo", Toast.LENGTH_LONG).show();
        }
    }
}
