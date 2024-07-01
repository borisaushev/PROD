package ru.prodcontest.posts.create;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/posts/new")
public class CreatePostController {
    //TODO: сделать:
    /*
    0.0. create model on repository, controller, service, exc handler

    1. create posts table
        author_id, content, tags, created_at, likes_count, dislike_count
    2. validate token
    3.1. create posts repository, classes and parsers
    3.2. save post
    4. return post

    */
}
