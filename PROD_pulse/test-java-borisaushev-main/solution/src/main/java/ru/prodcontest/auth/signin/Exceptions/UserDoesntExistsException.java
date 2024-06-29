package ru.prodcontest.auth.signin.Exceptions;

public class UserDoesntExistsException extends RuntimeException {
    public UserDoesntExistsException(String message) {
        super(message);
    }
}
