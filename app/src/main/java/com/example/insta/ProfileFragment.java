package com.example.insta;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insta.adapters.ProfilePostAdapter;
import com.example.insta.managers.UserManager;
import com.example.insta.models.Post;
import com.example.insta.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private TextView tvUsername, tvFullName, tvBio, tvPostsCount, tvFollowers, tvFollowing;
    private Button btnLogout, btnEditProfile;
    private RecyclerView recyclerViewPosts;
    private ProfilePostAdapter postAdapter;
    private List<Post> userPosts;
    private SharedPreferences sharedPreferences;
    private UserManager userManager;
    private User currentUser;

    private boolean isDataLoaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        try {
            sharedPreferences = getActivity().getSharedPreferences("user_prefs", 0);
            userManager = new UserManager(getActivity());
            userPosts = new ArrayList<>();

            initViews(view);
            setupRecyclerView();
        } catch (Exception e) {
            Log.e("ProfileFragment", "Error in onCreateView", e);
            Toast.makeText(getActivity(), "Ошибка создания профиля", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isDataLoaded) {
            refreshData();
            isDataLoaded = true;
        }
    }

    private void initViews(View view) {
        try {
            tvUsername = view.findViewById(R.id.tvUsername);
            tvFullName = view.findViewById(R.id.tvFullName);
            tvBio = view.findViewById(R.id.tvBio);
            tvPostsCount = view.findViewById(R.id.tvPostsCount);
            tvFollowers = view.findViewById(R.id.tvFollowers);
            tvFollowing = view.findViewById(R.id.tvFollowing);
            btnLogout = view.findViewById(R.id.btnLogout);
            btnEditProfile = view.findViewById(R.id.btnEditProfile);
            recyclerViewPosts = view.findViewById(R.id.recyclerViewPosts);


            safeSetText(tvPostsCount, "0");
            safeSetText(tvFollowers, "0");
            safeSetText(tvFollowing, "0");

            if (tvFollowers != null) {
                tvFollowers.setOnClickListener(v -> openFollowers());
            }

            if (tvFollowing != null) {
                tvFollowing.setOnClickListener(v -> openFollowing());
            }


            View followersLabel = view.findViewById(R.id.followersLabel);
            if (followersLabel != null) {
                followersLabel.setOnClickListener(v -> openFollowers());
            }

            View followingLabel = view.findViewById(R.id.followingLabel);
            if (followingLabel != null) {
                followingLabel.setOnClickListener(v -> openFollowing());
            }

            if (btnLogout != null) {
                btnLogout.setOnClickListener(v -> logout());
            }

            if (btnEditProfile != null) {
                btnEditProfile.setOnClickListener(v -> editProfile());
            }
        } catch (Exception e) {
            Log.e("ProfileFragment", "Error in initViews", e);
            Toast.makeText(getActivity(), "Ошибка инициализации интерфейса", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFollowers() {
        try {
            if (getActivity() != null && currentUser != null) {
                Intent intent = new Intent(getActivity(), FollowersActivity.class);
                intent.putExtra("type", "followers");
                intent.putExtra("user_id", currentUser.getId());
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("ProfileFragment", "Error opening followers", e);
            Toast.makeText(getActivity(), "Ошибка открытия подписчиков", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFollowing() {
        try {
            if (getActivity() != null && currentUser != null) {
                Intent intent = new Intent(getActivity(), FollowersActivity.class);
                intent.putExtra("type", "following");
                intent.putExtra("user_id", currentUser.getId());
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("ProfileFragment", "Error opening following", e);
            Toast.makeText(getActivity(), "Ошибка открытия подписок", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecyclerView() {
        try {
            if (recyclerViewPosts != null && getActivity() != null) {
                postAdapter = new ProfilePostAdapter(getActivity(), userPosts);
                recyclerViewPosts.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                recyclerViewPosts.setAdapter(postAdapter);
            }
        } catch (Exception e) {
            Log.e("ProfileFragment", "Error setting up recycler view", e);
        }
    }

    private void loadUserData() {
        try {
            if (userManager != null) {
                currentUser = userManager.getCurrentUser();

                if (currentUser != null) {
                    safeSetText(tvUsername, currentUser.getUsername() != null ?
                            "@" + currentUser.getUsername() : "Пользователь");
                    safeSetText(tvFullName, currentUser.getFullName() != null ?
                            currentUser.getFullName() : "Имя не указано");
                    safeSetText(tvBio, currentUser.getBio() != null ?
                            currentUser.getBio() : "Добавьте описание профиля");

                    updateCounters();
                } else {
                    safeSetText(tvUsername, "Пользователь не найден");
                    safeSetText(tvFullName, "");
                    safeSetText(tvBio, "Войдите в аккаунт");
                    updateCountersWithDefaults();
                }
            }
        } catch (Exception e) {
            Log.e("ProfileFragment", "Error loading user data", e);
            Toast.makeText(getActivity(), "Ошибка загрузки данных пользователя", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserPosts() {
        try {
            if (userPosts != null) {
                userPosts.clear();

                if (currentUser != null && currentUser.getUsername() != null) {
                    String currentUsername = currentUser.getUsername();


                    List<Post> loadedPosts = loadPostsForUser(currentUsername);
                    userPosts.addAll(loadedPosts);

                    safeSetText(tvPostsCount, String.valueOf(loadedPosts.size()));

                    if (postAdapter != null) {
                        postAdapter.updatePosts(userPosts);
                    }

                    Log.d("ProfileFragment", "Loaded " + loadedPosts.size() + " posts for user: " + currentUsername);
                }
            }
        } catch (Exception e) {
            Log.e("ProfileFragment", "Error loading user posts", e);
            Toast.makeText(getActivity(), "Ошибка загрузки постов", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Post> loadPostsForUser(String username) {
        List<Post> posts = new ArrayList<>();

        try {
   
            if ("alex".equals(username)) {
                Post post1 = new Post("1", username, username, android.R.drawable.ic_menu_camera, "Мой первый пост!");
                post1.addLike("maria");
                post1.addLike("demo");
                posts.add(post1);

                Post post3 = new Post("3", username, username, android.R.drawable.ic_menu_share, "Еще один пост!");
                post3.addLike("maria");
                post3.addLike("demo");
                posts.add(post3);
            } else if ("maria".equals(username)) {
                Post post2 = new Post("2", username, username, android.R.drawable.ic_menu_gallery, "Прекрасный день!");
                post2.addLike("alex");
                posts.add(post2);
            } else if ("demo".equals(username)) {
                Post post4 = new Post("4", username, username, android.R.drawable.ic_menu_send, "Тестовый пост!");
                post4.addLike("alex");
                post4.addLike("maria");
                posts.add(post4);
            } else {
                Post post = new Post("5", username, username, android.R.drawable.ic_dialog_info, "Мой первый пост в Insta!");
                posts.add(post);
            }
        } catch (Exception e) {
            Log.e("ProfileFragment", "Error in loadPostsForUser", e);
        }

        return posts;
    }

    private void updateCounters() {
        try {
            if (currentUser != null) {

                int postsCount = userPosts != null ? userPosts.size() : 0;
                int followersCount = currentUser.getFollowersCount();
                int followingCount = currentUser.getFollowingCount();

                safeSetText(tvPostsCount, String.valueOf(postsCount));
                safeSetText(tvFollowers, String.valueOf(followersCount));
                safeSetText(tvFollowing, String.valueOf(followingCount));

                Log.d("ProfileFragment", String.format("Counters - Posts: %d, Followers: %d, Following: %d",
                        postsCount, followersCount, followingCount));
            } else {
                updateCountersWithDefaults();
            }
        } catch (Exception e) {
            Log.e("ProfileFragment", "Error updating counters", e);
            updateCountersWithDefaults();
        }
    }

    private void updateCountersWithDefaults() {
        safeSetText(tvPostsCount, "0");
        safeSetText(tvFollowers, "0");
        safeSetText(tvFollowing, "0");
    }

    private void safeSetText(TextView textView, String text) {
        if (textView != null && text != null) {
            textView.setText(text);
        }
    }

    private void logout() {
        try {
            if (userManager != null && getActivity() != null) {
                userManager.logout();
                Toast.makeText(getActivity(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        } catch (Exception e) {
            Log.e("ProfileFragment", "Error during logout", e);
            Toast.makeText(getActivity(), "Ошибка при выходе", Toast.LENGTH_SHORT).show();
        }
    }

    private void editProfile() {
        try {
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);

                if (currentUser != null) {
                    intent.putExtra("user_id", currentUser.getId());
                }
                startActivityForResult(intent, 1); 
            }
        } catch (Exception e) {
            Log.e("ProfileFragment", "Error opening edit profile", e);
            Toast.makeText(getActivity(), "Ошибка открытия редактирования", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            refreshData();
        }
    }

    public void refreshData() {
        try {
            if (isAdded() && getActivity() != null) {
                isDataLoaded = false; 
                loadUserData();
                loadUserPosts();
                isDataLoaded = true;
            }
        } catch (Exception e) {
            Log.e("ProfileFragment", "Error refreshing data", e);
            Toast.makeText(getActivity(), "Ошибка обновления профиля", Toast.LENGTH_SHORT).show();
        }
    }


    public void forceRefresh() {
        isDataLoaded = false;
        refreshData();
    }
}
