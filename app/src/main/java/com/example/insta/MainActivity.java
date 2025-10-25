package com.example.insta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences sharedPreferences;

    private static final int TIME_INTERVAL = 2000;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        if (!isUserLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        initViews();
        setupBottomNavigation();
        loadFragment(new FeedFragment());

        showWelcomeMessage();
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_feed) {
                    loadFragment(new FeedFragment());
                    return true;
                } else if (itemId == R.id.nav_search) {
                    openSearch();
                    return true;
                } else if (itemId == R.id.nav_camera) {
                    openCamera();
                    return true;
                } else if (itemId == R.id.nav_add) {
                    openCreatePost();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    loadFragment(new ProfileFragment());
                    return true;
                }
                return false;
            }
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_feed) {
                    scrollFeedToTop();
                } else if (itemId == R.id.nav_profile) {
                    refreshProfile();
                }
            }
        });
    }

    // ИЗМЕНИТЕ С private НА public
    public void loadFragment(Fragment fragment) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка загрузки раздела", Toast.LENGTH_SHORT).show();
        }
    }

    private void openCamera() {
        try {
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Камера недоступна", Toast.LENGTH_SHORT).show();
        }
    }

    private void openCreatePost() {
        try {
            Intent intent = new Intent(MainActivity.this, CreatePostActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка открытия создания поста", Toast.LENGTH_SHORT).show();
        }
    }

    private void openSearch() {
        try {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Поиск временно недоступен", Toast.LENGTH_SHORT).show();
        }
    }

    private void scrollFeedToTop() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof FeedFragment) {
            ((FeedFragment) currentFragment).scrollToTop();
        } else {
            Toast.makeText(this, "Прокрутка доступна только в ленте", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshProfile() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof ProfileFragment) {
            ((ProfileFragment) currentFragment).refreshData();
        } else {
            Toast.makeText(this, "Обновление доступно только в профиле", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("is_logged_in", false);
    }

    private void showWelcomeMessage() {
        String username = sharedPreferences.getString("username", "");
        if (!username.isEmpty()) {
            Toast.makeText(this, "Добро пожаловать, " + username + "!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
            return;
        } else {
            Toast.makeText(this, "Нажмите еще раз для выхода", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isUserLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}