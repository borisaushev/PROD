package ru.prodcontest.userInfo.Exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String userAlreadyExists) {
        super(userAlreadyExists);
    }
}
