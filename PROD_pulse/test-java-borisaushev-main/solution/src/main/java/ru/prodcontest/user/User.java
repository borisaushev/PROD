package ru.prodcontest.user;

public record User(String login, String password, String email, String countryCode, boolean isPublic, String phone, String image) {}