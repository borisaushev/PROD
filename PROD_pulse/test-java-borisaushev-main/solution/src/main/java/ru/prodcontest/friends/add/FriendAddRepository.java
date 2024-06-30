package ru.prodcontest.friends.add;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FriendAddRepository {


    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public void addFriend(int requestAuthorId, int friendToAddId) {

        var map = new MapSqlParameterSource()
                .addValue("requestAuthorId", requestAuthorId)
                .addValue("friendToAddId", friendToAddId);

        jdbcTemplate.update("INSERT INTO friends(id, friend_with, added_at) VALUES (:requestAuthorId, :friendToAddId, now())", map);


    }

}
