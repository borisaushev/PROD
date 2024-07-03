package ru.prodcontest.posts.create;

import java.util.ArrayList;

public record CreatePostRequestObject(String content, ArrayList<String> tags) {

}
