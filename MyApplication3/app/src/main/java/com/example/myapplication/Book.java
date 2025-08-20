package com.example.myapplication;

public class Book {
    String title;
    String content;

    public Book(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}