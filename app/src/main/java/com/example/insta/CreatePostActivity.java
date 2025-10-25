package com.example.insta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreatePostActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_CAMERA_ACTIVITY = 3;
    private static final int REQUEST_PERMISSIONS = 100;

    private ImageView ivSelectedImage;
    private EditText etCaption;
    private Button btnPost;
    private LinearLayout layoutCameraOptions;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        initViews();
        setupClickListeners();
        checkPermissions();
    }

    private void initViews() {
        ivSelectedImage = findViewById(R.id.ivSelectedImage);
        etCaption = findViewById(R.id.etCaption);
        btnPost = findViewById(R.id.btnPost);
        layoutCameraOptions = findViewById(R.id.layoutCameraOptions);
    }

    private void setupClickListeners() {
        findViewById(R.id.btnSelectImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSelectionOptions();
            }
        });

        findViewById(R.id.btnTakePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        findViewById(R.id.btnChooseFromGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPost();
            }
        });
    }

    private void checkPermissions() {
        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        boolean allPermissionsGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (!allGranted) {
                Toast.makeText(this, "Разрешения необходимы для работы приложения", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showImageSelectionOptions() {
        if (layoutCameraOptions.getVisibility() == View.VISIBLE) {
            layoutCameraOptions.setVisibility(View.GONE);
        } else {
            layoutCameraOptions.setVisibility(View.VISIBLE);
        }
    }

    private void openCamera() {
        try {
            Intent intent = new Intent(CreatePostActivity.this, CameraActivity.class);
            startActivityForResult(intent, REQUEST_CAMERA_ACTIVITY);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Камера недоступна", Toast.LENGTH_SHORT).show();
        }
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            layoutCameraOptions.setVisibility(View.GONE);

            if (requestCode == REQUEST_CAMERA) {
   
                if (currentPhotoPath != null) {
                    ivSelectedImage.setImageURI(Uri.parse(currentPhotoPath));
                    btnPost.setEnabled(true);
                }
            } else if (requestCode == REQUEST_GALLERY) {

                if (data != null && data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    ivSelectedImage.setImageURI(selectedImageUri);
                    currentPhotoPath = getPathFromUri(selectedImageUri);
                    btnPost.setEnabled(true);
                }
            } else if (requestCode == REQUEST_CAMERA_ACTIVITY) {

                if (data != null && data.hasExtra("photo_path")) {
                    currentPhotoPath = data.getStringExtra("photo_path");
                    if (currentPhotoPath != null) {
                        File imgFile = new File(currentPhotoPath);
                        if (imgFile.exists()) {
                            ivSelectedImage.setImageURI(Uri.fromFile(imgFile));
                            btnPost.setEnabled(true);
                        }
                    }
                }
            }
        }
    }


    private String getPathFromUri(Uri contentUri) {

        return contentUri.toString();
    }
    private void saveBitmapToFile(Bitmap bitmap) {
        try {
            File file = createImageFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
            currentPhotoPath = file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void createPost() {
        String caption = etCaption.getText().toString().trim();

        if (currentPhotoPath == null) {
            Toast.makeText(this, "Выберите изображение", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Пост опубликован!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
