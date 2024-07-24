package ru.prodcontest.posts.post.exception;

public class PostIsPrivateException extends RuntimeException{

    public PostIsPrivateException(String message) {
        super(message);
    }
}
