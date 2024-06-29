package ru.prodcontest.auth.signin.token.Exceptions;

public class TokenDoesntExistException  extends RuntimeException {
    public TokenDoesntExistException(String msg) {
        super(msg);
    }
}
