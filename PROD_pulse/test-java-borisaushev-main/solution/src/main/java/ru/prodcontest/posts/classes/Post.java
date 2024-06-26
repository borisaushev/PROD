package ru.prodcontest.posts.classes;

import org.json.JSONArray;

import java.util.Date;

public record Post(String id, String content, String author, JSONArray tags, Date createdAt, int likesCount,
                   int dislikesCount) {
}
