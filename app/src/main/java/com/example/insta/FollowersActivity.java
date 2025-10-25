package com.example.insta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insta.adapters.UserAdapter;
import com.example.insta.managers.UserManager;
import com.example.insta.User;

import java.util.ArrayList;
import java.util.List;

public class FollowersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private UserManager userManager;
    private TextView tvTitle;
    private String type; // "followers" или "following"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);

        userManager = new UserManager(this);

        initViews();
        loadData();
    }

    private void initViews() {
        try {
            recyclerView = findViewById(R.id.recyclerView);
            tvTitle = findViewById(R.id.tvTitle);

            ImageView ivBack = findViewById(R.id.ivBack);
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            type = getIntent().getStringExtra("type");
            if (type == null) {
                type = "followers";
            }

            if ("followers".equals(type)) {
                tvTitle.setText("Подписчики");
            } else {
                tvTitle.setText("Подписки");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка инициализации", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadData() {
        try {
            userAdapter = new UserAdapter(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(userAdapter);

            List<User> users = getUsersList();
            if (users.isEmpty()) {
                Toast.makeText(this, "Список пуст", Toast.LENGTH_SHORT).show();
            } else {
                userAdapter.setUsers(users);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
        }
    }

    private List<User> getUsersList() {
        List<User> allUsers = userManager.getAllUsers();
        List<User> filteredUsers = new ArrayList<>();

        if ("followers".equals(type)) {
            User currentUser = userManager.getCurrentUser();
            for (User user : allUsers) {
                if (currentUser == null || !user.getUsername().equals(currentUser.getUsername())) {
                    filteredUsers.add(user);
                }
            }
        } else {
            User currentUser = userManager.getCurrentUser();
            for (User user : allUsers) {
                if (currentUser == null || !user.getUsername().equals(currentUser.getUsername())) {
                    filteredUsers.add(user);
                }
            }
        }

        return filteredUsers;
    }
}
