package com.example.insta.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.insta.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final String USERS_KEY = "app_users";
    private static final String CURRENT_USER_KEY = "current_user";
    private SharedPreferences sharedPreferences;

    public UserManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }

    public boolean registerUser(User user) {
        List<User> users = getAllUsers();


        users.add(user);
        saveAllUsers(users);
        return true;
    }


    public boolean loginUser(String username, String password) {
        List<User> users = getAllUsers();

        for (User user : users) {
            if (user.getUsername().equals(username)) {

                setCurrentUser(user);
                return true;
            }
        }
        return false;
    }

    public User getCurrentUser() {
        try {
            String userJson = sharedPreferences.getString(CURRENT_USER_KEY, null);
            if (userJson != null && !userJson.isEmpty()) {
                return userFromJson(userJson);
            }
        } catch (Exception e) {
            e.printStackTrace();

            logout();
        }
        return null;
    }


    public void setCurrentUser(User user) {
        try {
            String userJson = userToJson(user);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(CURRENT_USER_KEY, userJson);
            editor.putBoolean("is_logged_in", true);
            editor.putString("username", user.getUsername());
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CURRENT_USER_KEY);
        editor.putBoolean("is_logged_in", false);
        editor.remove("username");
        editor.apply();
    }


    public User getUserByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }

        List<User> users = getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }


    public List<User> getAllUsers() {
        String usersJson = sharedPreferences.getString(USERS_KEY, "[]");
        List<User> users = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(usersJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                User user = userFromJson(jsonObject.toString());
                if (user != null && user.getUsername() != null) {
                    users.add(user);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return users;
    }

    private void saveAllUsers(List<User> users) {
        JSONArray jsonArray = new JSONArray();

        for (User user : users) {
            try {
                JSONObject jsonObject = userToJsonObject(user);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERS_KEY, jsonArray.toString());
        editor.apply();
    }

    public boolean updateUser(User user) {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", user.getUsername());
            editor.putString("fullName", user.getFullName());
            editor.putString("bio", user.getBio());

            if (user.getProfilePhoto() != null) {
                editor.putString("profilePhoto", user.getProfilePhoto());
            }

            return editor.commit();
        } catch (Exception e) {
            Log.e("UserManager", "Error updating user", e);
            return false;
        }
    }


    public List<User> searchUsers(String query) {
        if (query == null || query.isEmpty()) {
            return getAllUsers();
        }

        List<User> users = getAllUsers();
        List<User> result = new ArrayList<>();

        for (User user : users) {
            if ((user.getUsername() != null && user.getUsername().toLowerCase().contains(query.toLowerCase())) ||
                    (user.getFullName() != null && user.getFullName().toLowerCase().contains(query.toLowerCase()))) {
                result.add(user);
            }
        }
        return result;
    }


    private JSONObject userToJsonObject(User user) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", user.getId() != null ? user.getId() : "");
        jsonObject.put("username", user.getUsername() != null ? user.getUsername() : "");
        jsonObject.put("fullName", user.getFullName() != null ? user.getFullName() : "");
        jsonObject.put("bio", user.getBio() != null ? user.getBio() : "");
        jsonObject.put("profileImage", user.getProfilePhoto() != null ? user.getProfilePhoto() : "");
        jsonObject.put("followersCount", user.getFollowersCount());
        jsonObject.put("followingCount", user.getFollowingCount());
        return jsonObject;
    }

    private String userToJson(User user) {
        try {
            JSONObject jsonObject = userToJsonObject(user);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "{}";
        }
    }


    private User userFromJson(String json) {
        try {

                return createDefaultUser();
            }

            JSONObject jsonObject = new JSONObject(json);
            User user = new User();
            user.setId(jsonObject.optString("id", ""));
            user.setUsername(jsonObject.optString("username", ""));
            user.setFullName(jsonObject.optString("fullName", ""));
            user.setBio(jsonObject.optString("bio", "Добро пожаловать в мой профиль!"));
            user.setProfilePhoto(jsonObject.optString("profileImage", ""));
            user.setFollowersCount(jsonObject.optInt("followersCount", 0));
            user.setFollowingCount(jsonObject.optInt("followingCount", 0));


            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return createDefaultUser();
        }
    }


    private User createDefaultUser() {
        User user = new User();
        user.setId(String.valueOf(System.currentTimeMillis()));
        user.setUsername("user");
        user.setFullName("Пользователь");
        user.setBio("Добро пожаловать в мой профиль!");
        return user;
    }
}
