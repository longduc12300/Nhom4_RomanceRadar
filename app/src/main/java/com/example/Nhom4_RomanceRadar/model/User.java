package com.example.Nhom4_RomanceRadar.model;

public class User {
    public String uid;
    public String displayName;
    public String email;
    public String photoUrl;

    public String bio;
    public String birth;

    public String image1;
    public String image2;
    public String image3;
    public boolean gender;

    public User() {

    }

    public User(String uid, String displayName, String email, String photoUrl, String bio, String birth, String image1, String image2, String image3, boolean gender) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.bio = bio;
        this.birth = birth;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.gender = gender;
    }


}