package com.example.insta.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insta.OtherProfileActivity;
import com.example.insta.R;
import com.example.insta.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<User> userList;

    public UserAdapter(Context context) {
        this.context = context;
        this.userList = new ArrayList<>();
    }

    public void setUsers(List<User> users) {
        this.userList = users;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = userList.get(position);

        if (user != null) {
            holder.tvUsername.setText(user.getUsername() != null ? user.getUsername() : "Пользователь");
            holder.tvFullName.setText(user.getFullName() != null ? user.getFullName() : "Имя не указано");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
                            Intent intent = new Intent(context, OtherProfileActivity.class);
                            intent.putExtra("username", user.getUsername());
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "Ошибка: имя пользователя не указано", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Ошибка открытия профиля", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUsername, tvFullName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvFullName = itemView.findViewById(R.id.tvFullName);
        }
    }
}