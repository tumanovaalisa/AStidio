package com.example.astidio;

public class Teacher {
    private int imageResId;
    private String name;
    private String danceType;

    public Teacher(int imageResId, String name, String danceType) {
        this.imageResId = imageResId;
        this.name = name;
        this.danceType = danceType;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }

    public String getDanceType() {
        return danceType;
    }
}
