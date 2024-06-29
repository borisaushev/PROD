package ru.prodcontest.countries.Exceptions;

public class NoSuchCountryException extends RuntimeException{
    public NoSuchCountryException(String mes) {
        super(mes);
    }
}
