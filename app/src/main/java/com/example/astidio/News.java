package com.example.astidio;

import java.util.Objects;

public class News {
    private String title;
    private String imageUrl;
    private String description;

    public News() {
    }
    // Конструктор класса
    public News(String title, String imageUrl, String description) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    // Методы доступа (геттеры и сеттеры) для каждого поля

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        News news = (News) obj;
        return title.equals(news.title) &&
                imageUrl.equals(news.imageUrl) &&
                description.equals(news.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, imageUrl, description);
    }
}

