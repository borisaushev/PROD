package ru.prodcontest.posts.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CreatePostRepository {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    public void doSmth() {

    }

}
