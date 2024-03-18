package ru.prodcontest.DataBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.prodcontest.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Component
public class UserTableUtil {
    private static boolean tableIsCreated = false;
    @Autowired
    private SqlPreparedStatements preparedStatements;

    public void createTableIfNeeded() {
        if(tableIsCreated)
            return;

        try {

            PreparedStatement prepStatement = preparedStatements.getTableCreationPreparedStatement();
            tableIsCreated = true;
            prepStatement.execute();

        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    public boolean userExists(User user) {
        try {
            PreparedStatement prepStatement = preparedStatements.getUserAlreadyExistsPreparedStatement(user);

            ResultSet resultSet = prepStatement.executeQuery();
            resultSet.next();

            boolean userIsUnique = resultSet.getBoolean(1);

            return userIsUnique;

        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }

    }

    public boolean userExists(String login, String password) {
        try {
            PreparedStatement prepStatement = preparedStatements.
                    getUserAlreadyExistsPreparedStatement(login, password);

            ResultSet resultSet = prepStatement.executeQuery();
            resultSet.next();

            boolean userIsUnique = resultSet.getBoolean(1);

            return userIsUnique;

        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }

    }

    public boolean phoneNumberAlreadyTaken(String phoneNumber) {
        try {
            PreparedStatement prepStatement = preparedStatements.
                    getPhoneNumberAlreadyTaken(phoneNumber);

            ResultSet resultSet = prepStatement.executeQuery();
            resultSet.next();

            boolean phoneNumberAlreadyTaken = resultSet.getBoolean(1);

            return phoneNumberAlreadyTaken;

        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    public boolean userDontExists(String login) {
        try {
            PreparedStatement prepStatement = preparedStatements.
                    getUserDontExistsPreparedStatement(login);

            ResultSet resultSet = prepStatement.executeQuery();
            resultSet.next();

            boolean userExists = resultSet.getBoolean(1);

            return !userExists;

        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }

    }

    public void saveUser(User user) {
        try {
            PreparedStatement preparedStatement = preparedStatements.getSaveUserPreparedStatement(user);
            preparedStatement.execute();
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    public User getUserByLogin(String login) {
        try {
            PreparedStatement preparedStatement = preparedStatements.getFindUserByLoginPreparedStatement(login);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            String password = resultSet.getString("password");
            String email = resultSet.getString("email");
            String countryCode = resultSet.getString("countryCode");
            boolean isPublic = resultSet.getBoolean("isPublic");
            String phone = resultSet.getString("phone");
            String image = resultSet.getString("image");

            User user = new User(login, password, email, countryCode, isPublic, phone, image);

            return user;
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    public void updateUserInfo(String oldLogin, User newUserData) {
        try {
            //Deleting the account
            PreparedStatement preparedStatement = preparedStatements.getDeleteUserDataPreparedStatement(oldLogin);
            preparedStatement.execute();

            //Creating new account
            preparedStatement = preparedStatements.getSaveUserPreparedStatement(newUserData);
            preparedStatement.execute();

        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

}
