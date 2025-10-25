package com.example.insta.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insta.OtherProfileActivity;
import com.example.insta.R;
import com.example.insta.models.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context context;
    private List<Post> postList;
    private SharedPreferences sharedPreferences;
    private OnPostInteractionListener listener;

    public interface OnPostInteractionListener {
        void onLikeClicked(Post post);
        void onCommentClicked(Post post);
        void onUserProfileClicked(String username);
    }

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
        this.sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }

    public void setOnPostInteractionListener(OnPostInteractionListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = postList.get(position);
        String currentUserId = sharedPreferences.getString("username", "");

        holder.tvUsername.setText(post.getUsername());
        holder.tvCaption.setText(post.getCaption());
        holder.tvLikesCount.setText(post.getLikesCount() + " лайков");

        // Устанавливаем изображение поста
        if (post.hasImageResource()) {
            // Используем локальный ресурс
            holder.ivPostImage.setImageResource(post.getImageResource());
        } else {
            // Используем стандартное изображение если URL не доступен
            holder.ivPostImage.setImageResource(android.R.color.darker_gray);
        }

        // Устанавливаем иконку лайка в зависимости от состояния
        if (post.isLikedByUser(currentUserId)) {
            holder.ivLike.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            holder.ivLike.setImageResource(android.R.drawable.btn_star_big_off);
        }

        // Обработчик клика на имя пользователя
        holder.tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onUserProfileClicked(post.getUsername());
                }
            }
        });

        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLikeClicked(post);
                }
            }
        });

        holder.ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCommentClicked(post);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void updatePost(Post updatedPost) {
        for (int i = 0; i < postList.size(); i++) {
            if (postList.get(i).getId().equals(updatedPost.getId())) {
                postList.set(i, updatedPost);
                notifyItemChanged(i);
                break;
            }
        }
    }

    // Метод для обновления всего списка
    public void updatePosts(List<Post> newPosts) {
        postList.clear();
        postList.addAll(newPosts);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUsername, tvCaption, tvLikesCount;
        public ImageView ivPostImage, ivLike, ivComment;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            tvLikesCount = itemView.findViewById(R.id.tvLikesCount);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            ivLike = itemView.findViewById(R.id.ivLike);
            ivComment = itemView.findViewById(R.id.ivComment);
        }
    }
}