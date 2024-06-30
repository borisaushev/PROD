package ru.prodcontest.friends.remove;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FriendRemoveRepository {


    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public void removeFriend(int requestAuthorId, int friendToRemoveId) {

        var map = new MapSqlParameterSource()
                .addValue("requestAuthorId", requestAuthorId)
                .addValue("friendToRemoveId", friendToRemoveId);

        jdbcTemplate.update("DELETE FROM friends WHERE id = :requestAuthorId AND friend_with = :friendToRemoveId", map);


    }

}
