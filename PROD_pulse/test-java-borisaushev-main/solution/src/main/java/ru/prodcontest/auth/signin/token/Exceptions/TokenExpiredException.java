package ru.prodcontest.auth.signin.token.Exceptions;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String msg) {
        super(msg);
    }
}
