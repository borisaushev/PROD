package ru.prodcontest.DataBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.prodcontest.user.User;
import ru.prodcontest.user.UserDataUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component("SqlPreparedStatements")
public class SqlPreparedStatements {
    @Autowired
    private Connection connection;
    public PreparedStatement getFindUserByLoginPreparedStatement(String login) throws SQLException {
        PreparedStatement prepStatement = connection.prepareStatement(
                "SELECT * FROM users WHERE login=?"
        );
        prepStatement.setString(1, login);

        return prepStatement;

    }

    public PreparedStatement getPhoneNumberAlreadyTaken(String phoneNumber) throws SQLException {
        PreparedStatement prepStatement = connection.prepareStatement(
                "SELECT EXISTS(SELECT login FROM users WHERE phone=?)"
        );

        prepStatement.setString(1, phoneNumber);

        return prepStatement;
    }

    public PreparedStatement getUserAlreadyExistsPreparedStatement(User user) throws SQLException {
        /*
        SELECT EXISTS(SELECT login FROM users WHERE login='login' OR email='' OR phone='');
         */
        PreparedStatement prepStatement = connection.prepareStatement(
                "SELECT EXISTS(SELECT login FROM users WHERE login=? OR email=? OR phone=?)"
        );

        prepStatement.setString(1, user.login);
        prepStatement.setString(2, user.email);
        prepStatement.setString(3, user.phone);

        return prepStatement;

    }

    public PreparedStatement getUserDontExistsPreparedStatement(String login) throws SQLException {
        PreparedStatement prepStatement = connection.prepareStatement(
                "SELECT EXISTS(SELECT login FROM users WHERE login=?)"
        );

        prepStatement.setString(1, login);

        return prepStatement;

    }

    public PreparedStatement getUserAlreadyExistsPreparedStatement(String login, String password) throws SQLException {
        PreparedStatement prepStatement = connection.prepareStatement(
                "SELECT EXISTS(SELECT * FROM users WHERE login=? AND password=?)"
        );

        prepStatement.setString(1, login);
        prepStatement.setString(2, UserDataUtil.hashPassword(password));

        return prepStatement;

    }

    public PreparedStatement getTableCreationPreparedStatement() throws SQLException {
        PreparedStatement prepStatement = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS users (
                        login TEXT PRIMARY KEY,
                        password TEXT,
                        email TEXT UNIQUE,
                        phone TEXT UNIQUE,
                        countryCode TEXT,
                        isPublic BOOLEAN,
                        image TEXT
                    )
                    """);
        return prepStatement;
    }

    public PreparedStatement getSaveUserPreparedStatement(User user) throws SQLException {
        PreparedStatement prepStatement = connection.prepareStatement(
                "INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?)"
        );

        prepStatement.setString(1, user.login);
        prepStatement.setString(2, UserDataUtil.hashPassword(user.password));
        prepStatement.setString(3, user.email);
        prepStatement.setString(4, user.phone);
        prepStatement.setString(5, user.countryCode);
        prepStatement.setBoolean(6, user.isPublic);
        prepStatement.setString(7, user.image);

        return prepStatement;
    }

    public PreparedStatement getDeleteUserDataPreparedStatement(String oldLogin) throws SQLException {
        PreparedStatement prepStatement = connection.prepareStatement(
                "DELETE FROM users WHERE login=?"
        );
        prepStatement.setString(1, oldLogin);

        return prepStatement;
    }

}
