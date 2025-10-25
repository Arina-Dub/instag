package com.example.insta;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insta.models.Post;

public class PostDetailActivity extends AppCompatActivity {
    private ImageView ivPostImage, ivLike, ivBack;
    private TextView tvUsername, tvCaption, tvLikesCount;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        initViews();
        loadPostData();
    }

    private void initViews() {
        try {
            ivPostImage = findViewById(R.id.ivPostImage);
            ivLike = findViewById(R.id.ivLike);
            ivBack = findViewById(R.id.ivBack);
            tvUsername = findViewById(R.id.tvUsername);
            tvCaption = findViewById(R.id.tvCaption);
            tvLikesCount = findViewById(R.id.tvLikesCount);

            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleLike();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка инициализации интерфейса", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadPostData() {
        try {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("post_id")) {
                String postId = intent.getStringExtra("post_id");

                post = new Post(postId, "user1", "alex", android.R.drawable.ic_menu_camera, "Пример поста с изображением");
                post.addLike("user2");
                post.addLike("user3");

                updateUI();
            } else {
                Toast.makeText(this, "Ошибка загрузки поста", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка загрузки данных поста", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateUI() {
        if (post != null) {
            tvUsername.setText(post.getUsername());
            tvCaption.setText(post.getCaption());
            tvLikesCount.setText(post.getLikesCount() + " лайков");

            if (post.hasImageResource()) {
                ivPostImage.setImageResource(post.getImageResource());
            }

            if (post.isLikedByUser("current_user")) {
                ivLike.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                ivLike.setImageResource(android.R.drawable.btn_star_big_off);
            }
        }
    }

    private void toggleLike() {
        if (post != null) {
            if (post.isLikedByUser("current_user")) {
                post.removeLike("current_user");
                Toast.makeText(this, "Лайк удален", Toast.LENGTH_SHORT).show();
            } else {
                post.addLike("current_user");
                Toast.makeText(this, "Лайк добавлен", Toast.LENGTH_SHORT).show();
            }
            updateUI();
        }
    }
}
