package com.example.insta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.insta.adapters.UserAdapter;
import com.example.insta.managers.UserManager;
import com.example.insta.User;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private EditText etSearch;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        userManager = new UserManager(this);
        initViews();
        setupRecyclerView();
        setupSearch();
    }

    private void initViews() {
        try {
            etSearch = findViewById(R.id.etSearch);
            recyclerView = findViewById(R.id.recyclerView);

            ImageView ivBack = findViewById(R.id.ivBack);
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка инициализации", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupRecyclerView() {
        try {
            userAdapter = new UserAdapter(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(userAdapter);

            // Показываем всех пользователей при запуске
            List<User> allUsers = userManager.getAllUsers();
            if (allUsers.isEmpty()) {
                Toast.makeText(this, "Пользователи не найдены", Toast.LENGTH_SHORT).show();
            } else {
                userAdapter.setUsers(allUsers);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка настройки списка", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSearch() {
        try {
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchUsers(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchUsers(String query) {
        try {
            if (query.isEmpty()) {
                List<User> allUsers = userManager.getAllUsers();
                userAdapter.setUsers(allUsers);
            } else {
                List<User> searchResults = userManager.searchUsers(query);
                userAdapter.setUsers(searchResults);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}