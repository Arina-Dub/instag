package com.example.insta.models;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private String id;
    private String userId;
    private String username;
    private String imageUrl;
    private int imageResource; // Добавляем поле для ресурсов изображений
    private String caption;
    private long timestamp;
    private List<String> likes;
    private int commentsCount;

    public Post() {
        this.likes = new ArrayList<>();
        this.imageResource = -1; // -1 означает, что ресурс не установлен
    }

    public Post(String id, String userId, String username, String imageUrl, String caption) {
        this();
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.timestamp = System.currentTimeMillis();
    }

    // Новый конструктор для работы с ресурсами изображений
    public Post(String id, String userId, String username, int imageResource, String caption) {
        this();
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.imageResource = imageResource;
        this.caption = caption;
        this.timestamp = System.currentTimeMillis();
    }

    // Геттеры и сеттеры
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getImageResource() { return imageResource; }
    public void setImageResource(int imageResource) { this.imageResource = imageResource; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public List<String> getLikes() { return likes; }
    public void setLikes(List<String> likes) { this.likes = likes; }

    public int getLikesCount() { return likes.size(); }

    public int getCommentsCount() { return commentsCount; }
    public void setCommentsCount(int commentsCount) { this.commentsCount = commentsCount; }

    // Методы для работы с лайками
    public void addLike(String userId) {
        if (!likes.contains(userId)) {
            likes.add(userId);
        }
    }

    public void removeLike(String userId) {
        likes.remove(userId);
    }

    public boolean isLikedByUser(String userId) {
        return likes.contains(userId);
    }

    // Проверка, используется ли ресурс изображения
    public boolean hasImageResource() {
        return imageResource != -1;
    }
}