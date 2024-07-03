package ru.prodcontest.posts.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreatePostService {
    @Autowired
    CreatePostRepository repository;

    public Object serve() {
        return null;
    }

}
