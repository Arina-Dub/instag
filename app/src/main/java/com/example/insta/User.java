package com.example.insta;

public class User {
    private String id;
    private String username;
    private String fullName;
    private String bio;
    private String profilePhoto;
    private int followersCount;
    private int followingCount;


    public User() {
        this.followersCount = 0;
        this.followingCount = 0;
    }


    public User(String id, String username, String fullName, String bio) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.bio = bio;
        this.followersCount = 0;
        this.followingCount = 0;
        this.profilePhoto = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }


    public void addFollower() {
        this.followersCount++;
    }

    public void removeFollower() {
        if (this.followersCount > 0) {
            this.followersCount--;
        }
    }

    public void addFollowing() {
        this.followingCount++;
    }

    public void removeFollowing() {
        if (this.followingCount > 0) {
            this.followingCount--;
        }
    }


    public boolean isValid() {
        return id != null && username != null && !username.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", bio='" + bio + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", followersCount=" + followersCount +
                ", followingCount=" + followingCount +
                '}';
    }
}
