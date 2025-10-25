package com.example.insta.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insta.PostDetailActivity;
import com.example.insta.R;
import com.example.insta.models.Post;

import java.util.List;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ViewHolder> {
    private Context context;
    private List<Post> postList;

    public ProfilePostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_profile_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = postList.get(position);

        if (post != null) {

            if (post.hasImageResource()) {
                holder.ivPostImage.setImageResource(post.getImageResource());
            } else {
                holder.ivPostImage.setImageResource(android.R.color.darker_gray);
            }

            holder.tvLikesCount.setText(post.getLikesCount() + " ❤");
            holder.tvCommentsCount.setText(post.getCommentsCount() + " 💬");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(context, PostDetailActivity.class);
                        intent.putExtra("post_id", post.getId());
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Ошибка открытия поста", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (holder.layoutOverlay != null) {
                        holder.layoutOverlay.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void updatePosts(List<Post> newPosts) {
        postList.clear();
        postList.addAll(newPosts);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivPostImage;
        public TextView tvLikesCount, tvCommentsCount;
        public View layoutOverlay;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            tvLikesCount = itemView.findViewById(R.id.tvLikesCount);
            tvCommentsCount = itemView.findViewById(R.id.tvCommentsCount);
            layoutOverlay = itemView.findViewById(R.id.layoutOverlay);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layoutOverlay != null) {
                        layoutOverlay.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
