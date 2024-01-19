package com.example.astidio;

public class Teacher {
    private String imageResId;
    private String name;
    private String danceType;

    public Teacher(String imageResId, String name, String danceType) {
        this.imageResId = imageResId;
        this.name = name;
        this.danceType = danceType;
    }

    public String getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }

    public String getDanceType() {
        return danceType;
    }
}
