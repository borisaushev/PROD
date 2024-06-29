package ru.prodcontest.user.Exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String userAlreadyExists) {
        super(userAlreadyExists);
    }
}
