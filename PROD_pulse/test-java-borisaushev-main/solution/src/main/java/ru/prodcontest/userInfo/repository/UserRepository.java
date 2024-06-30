package ru.prodcontest.userInfo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.prodcontest.userInfo.User;

import java.util.List;

@Repository
@Transactional
public class UserRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public boolean userExists(User user) {

        var map = new MapSqlParameterSource()
                .addValue("login", user.login())
                .addValue("email", user.email())
                .addValue("phone", user.phone());

        boolean userAlreadyExists = Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM users WHERE login = :login OR email = :email OR user_phone = :phone)", map, Boolean.class));

        return userAlreadyExists;

    }

    public boolean userExistsWithProperty(String columnName, Object value) {

        var map = new MapSqlParameterSource()
                .addValue("value", value);

        boolean userExists = Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM users WHERE " + columnName + " = :value)", map, Boolean.class));

        return userExists;

    }

    public boolean userExistsWithProperties(String[] columns, Object[] values) {

        String sql = "SELECT EXISTS(SELECT 1 FROM users WHERE ";
        var map = new MapSqlParameterSource();
        sql += columns[0] + " = :value0";
        map.addValue("value0", values[0]);

        for(int i = 1; i < columns.length; i++) {
            sql += " AND " + columns[i] + " = :value" + i;
            map.addValue("value" + i, values[i]);
        }
        sql += ")";

            boolean userExists = Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, map, Boolean.class));

            return userExists;
    }

    public boolean userExists(String login, String hashedPassword) {

        var map = new MapSqlParameterSource()
                .addValue("login", login)
                .addValue("password", hashedPassword);

        System.out.println(login + " : " + hashedPassword);
        boolean userExists = Boolean.TRUE.equals(jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM users WHERE login = :login AND password = :password)", map, Boolean.class));

        return userExists;

    }

    public int getUserId(String login, String hashedPassword) {

        var map = new MapSqlParameterSource()
                .addValue("login", login)
                .addValue("password", hashedPassword);

        int userId = jdbcTemplate.queryForObject("SELECT id FROM users WHERE login = :login AND password = :password", map, Integer.class);

        return userId;

    }

    public void saveUser(User user, String hashedPassword) {

        var map = new MapSqlParameterSource()
                .addValue("login", user.login())
                .addValue("hashedPassword", hashedPassword)
                .addValue("email", user.email())
                .addValue("user_phone", user.phone())
                .addValue("country_code", user.countryCode())
                .addValue("is_public", user.isPublic())
                .addValue("user_image", user.image());

        jdbcTemplate.update("INSERT INTO users(login, password, email, country_code, is_public, user_phone, user_image) " +
                                "VALUES (:login, :hashedPassword, :email, :country_code, :is_public, :user_phone, :user_image)", map);

    }

    public void saveTokenForUser(int userId, String token) {
        var map = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("token", token);

        jdbcTemplate.update("INSERT INTO tokens(id, token) " +
                "VALUES (:userId, :token)", map);
    }

    public List<String> getValidTokensById(int userId) {
        var map = new MapSqlParameterSource()
                .addValue("userId", userId);

        List<String> validTokens = jdbcTemplate.query("SELECT token FROM tokens WHERE id = :userId", map, (rs, rn) -> rs.getString(1));

        return validTokens;
    }

    public User getUserById(int userId) {
        var map = new MapSqlParameterSource()
                .addValue("userId", userId);
        String sql = "SELECT * FROM users WHERE id = :userId";
        User user = executeAndParseUserData(sql, map);

        return user;
    }

    private User executeAndParseUserData(String sql, MapSqlParameterSource map) {
        return jdbcTemplate.queryForObject(sql, map, (rs, rowNum) ->
                new User(
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("country_code"),
                        rs.getBoolean("is_public"),
                        rs.getString("user_phone"),
                        rs.getString("user_image")
                ));
    }

    public void updateUserProperty(int userId, String columnName, Object propertyValue) {

        var map = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("value", propertyValue);
        System.out.println("UPDATE users SET " + columnName + " = " + propertyValue + " WHERE id = " + userId);
        jdbcTemplate.update("UPDATE users SET " + columnName + " = :value WHERE id = :userId", map);

    }

    public void deleteTokensById(int userId) {
        var map = new MapSqlParameterSource()
                .addValue("userId", userId);
        jdbcTemplate.update("DELETE FROM tokens WHERE id = :userId", map);
    }

    public User getUserByLogin(String login) {
        var map = new MapSqlParameterSource()
                .addValue("login", login);
        String sql = "SELECT * FROM users WHERE login = :login";
        User user = executeAndParseUserData(sql, map);

        return user;
    }

    public List<Integer> getUserFriends(int userId) {

        var map = new MapSqlParameterSource()
                .addValue("userId", userId);

        List<Integer> friendsList = jdbcTemplate.query("SELECT friends_with FROM friends WHERE id = :userId", map, (rs, rn) -> rs.getInt(1));

        return friendsList;

    }

    public int getIdByLogin(String login) {

        var map = new MapSqlParameterSource()
                .addValue("login", login);

        int userId = jdbcTemplate.queryForObject("SELECT id FROM users WHERE login = :login", map, Integer.class);

        return userId;
    }

}
