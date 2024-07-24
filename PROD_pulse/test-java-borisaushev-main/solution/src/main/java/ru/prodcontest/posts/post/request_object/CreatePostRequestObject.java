package ru.prodcontest.posts.post.request_object;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreatePostRequestObject {
    private String content;
    private List<String> tags;
}
