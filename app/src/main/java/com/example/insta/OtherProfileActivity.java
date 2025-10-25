package com.example.insta;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insta.adapters.ProfilePostAdapter;
import com.example.insta.managers.UserManager;
import com.example.insta.models.Post;
import com.example.insta.User;

import java.util.ArrayList;
import java.util.List;

public class OtherProfileActivity extends AppCompatActivity {
    private TextView tvUsername, tvFullName, tvBio, tvPostsCount, tvFollowers, tvFollowing;
    private Button btnFollow, btnMessage;
    private RecyclerView recyclerViewPosts;
    private ProfilePostAdapter postAdapter;
    private List<Post> userPosts;
    private UserManager userManager;
    private User currentUser;
    private User profileUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        userManager = new UserManager(this);
        currentUser = userManager.getCurrentUser();

        initViews();

        if (!loadProfileData()) {
            return;
        }

        setupRecyclerView();
        loadUserPosts();
    }

    private void initViews() {
        try {
            tvUsername = findViewById(R.id.tvUsername);
            tvFullName = findViewById(R.id.tvFullName);
            tvBio = findViewById(R.id.tvBio);
            tvPostsCount = findViewById(R.id.tvPostsCount);
            tvFollowers = findViewById(R.id.tvFollowers);
            tvFollowing = findViewById(R.id.tvFollowing);
            btnFollow = findViewById(R.id.btnFollow);
            btnMessage = findViewById(R.id.btnMessage);
            recyclerViewPosts = findViewById(R.id.recyclerViewPosts);

            userPosts = new ArrayList<>();

            ImageView ivBack = findViewById(R.id.ivBack);
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleFollow();
                }
            });

            btnMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessage();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка инициализации интерфейса", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean loadProfileData() {
        try {
            String username = getIntent().getStringExtra("username");
            if (username == null || username.isEmpty()) {
                Toast.makeText(this, "Не указано имя пользователя", Toast.LENGTH_SHORT).show();
                finish();
                return false;
            }

            profileUser = userManager.getUserByUsername(username);
            if (profileUser == null) {
                Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
                finish();
                return false;
            }

            updateProfileUI();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка загрузки профиля", Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }
    }

    private void updateProfileUI() {
        if (profileUser != null) {
            tvUsername.setText(profileUser.getUsername());
            tvFullName.setText(profileUser.getFullName());
            tvBio.setText(profileUser.getBio());
            tvFollowers.setText(String.valueOf(profileUser.getFollowersCount()));
            tvFollowing.setText(String.valueOf(profileUser.getFollowingCount()));

            updateFollowButton();
        }
    }

    private void updateFollowButton() {
        if (isFollowing()) {
            btnFollow.setText("Отписаться");
            btnFollow.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_red_light));
        } else {
            btnFollow.setText("Подписаться");
            btnFollow.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_blue_light));
        }
    }

    private boolean isFollowing() {
        return Math.random() > 0.5;
    }

    private void setupRecyclerView() {
        try {
            postAdapter = new ProfilePostAdapter(this, userPosts);
            recyclerViewPosts.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerViewPosts.setAdapter(postAdapter);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка настройки списка постов", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserPosts() {
        try {
            userPosts.clear();

            if (profileUser != null) {
                Post post1 = new Post("1", profileUser.getId(), profileUser.getUsername(),
                        "https://example.com/image1.jpg", "Мой первый пост!");
                post1.addLike("user2");
                post1.addLike("user3");

                Post post2 = new Post("2", profileUser.getId(), profileUser.getUsername(),
                        "https://example.com/image2.jpg", "Отличный день!");
                post2.addLike("user1");

                userPosts.add(post1);
                userPosts.add(post2);

                postAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка загрузки постов", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleFollow() {
        try {
            if (profileUser != null) {
                if (isFollowing()) {
                    profileUser.setFollowersCount(profileUser.getFollowersCount() - 1);
                    Toast.makeText(this, "Вы отписались от " + profileUser.getUsername(), Toast.LENGTH_SHORT).show();
                } else {
                    profileUser.setFollowersCount(profileUser.getFollowersCount() + 1);
                    Toast.makeText(this, "Вы подписались на " + profileUser.getUsername(), Toast.LENGTH_SHORT).show();
                }

                userManager.updateUser(profileUser);
                updateProfileUI();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при подписке", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMessage() {
        Toast.makeText(this, "Сообщения скоро будут доступны", Toast.LENGTH_SHORT).show();
    }
}