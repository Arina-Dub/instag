package com.example.insta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.insta.managers.UserManager;

public class EditProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView ivProfilePhoto;
    private EditText etUsername, etFullName, etBio;
    private Button btnSave, btnChangePhoto, btnCancel;
    private UserManager userManager;
    private User currentUser;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        userManager = new UserManager(this);
        currentUser = userManager.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        loadUserData();
    }

    private void initViews() {
        try {
            ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
            etUsername = findViewById(R.id.etUsername);
            etFullName = findViewById(R.id.etFullName);
            etBio = findViewById(R.id.etBio);
            btnSave = findViewById(R.id.btnSave);
            btnChangePhoto = findViewById(R.id.btnChangePhoto);
            btnCancel = findViewById(R.id.btnCancel);

            btnChangePhoto.setOnClickListener(v -> openImagePicker());
            btnSave.setOnClickListener(v -> saveProfile());
            btnCancel.setOnClickListener(v -> finish());

        } catch (Exception e) {
            Toast.makeText(this, "Ошибка инициализации интерфейса", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            finish();
        }
    }

    private void loadUserData() {
        if (currentUser != null) {
            etUsername.setText(currentUser.getUsername() != null ? currentUser.getUsername() : "");
            etFullName.setText(currentUser.getFullName() != null ? currentUser.getFullName() : "");
            etBio.setText(currentUser.getBio() != null ? currentUser.getBio() : "");
        }
    }

    private void openImagePicker() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка при открытии галереи", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (ivProfilePhoto != null && selectedImageUri != null) {
                ivProfilePhoto.setImageURI(selectedImageUri);
                Toast.makeText(this, "Фото выбрано", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveProfile() {
        try {
            String username = etUsername.getText().toString().trim();
            String fullName = etFullName.getText().toString().trim();
            String bio = etBio.getText().toString().trim();

            if (username.isEmpty()) {
                Toast.makeText(this, "Введите имя пользователя", Toast.LENGTH_SHORT).show();
                return;
            }

            // Обновляем данные пользователя
            currentUser.setUsername(username);
            currentUser.setFullName(fullName);
            currentUser.setBio(bio);

            // Сохраняем новое фото если было выбрано
            if (selectedImageUri != null) {
                currentUser.setProfilePhoto(selectedImageUri.toString());
            }

            // Сохраняем изменения
            if (userManager.updateUser(currentUser)) {
                Toast.makeText(this, "Профиль успешно обновлен", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Ошибка сохранения профиля", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Ошибка при сохранении", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}