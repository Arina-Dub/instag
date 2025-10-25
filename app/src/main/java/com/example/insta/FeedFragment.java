package com.example.insta;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.insta.adapters.PostAdapter;
import com.example.insta.models.Post;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment implements PostAdapter.OnPostInteractionListener {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        sharedPreferences = getActivity().getSharedPreferences("user_prefs", 0);
        initViews(view);
        setupRecyclerView();
        loadPosts();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        postList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        postAdapter = new PostAdapter(getActivity(), postList);
        postAdapter.setOnPostInteractionListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(postAdapter);
    }

    private void loadPosts() {
        postList.clear();

        Post post1 = new Post("1", "user1", "alex", android.R.drawable.ic_menu_camera, "Мой первый пост с фото!");
        post1.addLike("maria");
        post1.addLike("demo");

        Post post2 = new Post("2", "user2", "maria", android.R.drawable.ic_menu_gallery, "Прекрасный день для фото!");
        post2.addLike("alex");

        Post post3 = new Post("3", "user1", "alex", android.R.drawable.ic_menu_share, "Делимся моментами!");
        post3.addLike("user2");
        post3.addLike("user3");
        post3.addLike("user4");

        Post post4 = new Post("4", "user3", "demo", android.R.drawable.ic_menu_send, "Тестовый пост с изображением");
        post4.addLike("user1");
        post4.addLike("user2");

        postList.add(post1);
        postList.add(post2);
        postList.add(post3);
        postList.add(post4);

        postAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLikeClicked(Post post) {
        String currentUserId = sharedPreferences.getString("username", "");

        if (post.isLikedByUser(currentUserId)) {
            post.removeLike(currentUserId);
            Toast.makeText(getActivity(), "Лайк удален", Toast.LENGTH_SHORT).show();
        } else {
            post.addLike(currentUserId);
            Toast.makeText(getActivity(), "Лайк добавлен", Toast.LENGTH_SHORT).show();
        }

        postAdapter.updatePost(post);
    }

    @Override
    public void onCommentClicked(Post post) {
        Toast.makeText(getActivity(), "Комментарии к посту " + post.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserProfileClicked(String username) {
        openUserProfile(username);
    }

    private void openUserProfile(String username) {
        String currentUser = sharedPreferences.getString("username", "");
        if (username.equals(currentUser)) {
            ((MainActivity) getActivity()).loadFragment(new ProfileFragment());
        } else {
            Intent intent = new Intent(getActivity(), OtherProfileActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
    }

    public void scrollToTop() {
        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    public void refreshFeed() {
        loadPosts();
        Toast.makeText(getActivity(), "Лента обновлена", Toast.LENGTH_SHORT).show();
    }
}
