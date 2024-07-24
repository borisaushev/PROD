package ru.prodcontest.friends.list;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import ru.prodcontest.friends.Friend;

import java.util.List;

@Repository
public class FriendsListRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Transactional
    public List<Friend> getFriendsList(int requestAuthorId, int limit, int offset) {

        var map = new MapSqlParameterSource()
                .addValue("requestAuthorId", requestAuthorId)
                .addValue("limit", limit)
                .addValue("offset", offset);

        List<Friend> friendList = jdbcTemplate.query(
                "SELECT friend_with, added_at FROM friends WHERE id = :requestAuthorId" +
                " ORDER BY added_at DESC LIMIT :limit OFFSET :offset",
                map, (rs, rm) -> new Friend(rs.getInt("friend_with"), rs.getTimestamp("added_at").toString()));

        return friendList;
    }


}
