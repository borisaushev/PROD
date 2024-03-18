package ru.prodcontest.user;

import lombok.Setter;

public class User {
    @Setter
    public String login;
    @Setter
    public String password;
    @Setter
    public String email;
    @Setter
    public String countryCode;
    @Setter
    public boolean isPublic;
    @Setter
    public String phone;
    @Setter
    public String image;

    public User(String login,
                String password,
                String email,
                String countryCode,
                boolean isPublic,
                String phone,
                String image) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.countryCode = countryCode;
        this.isPublic = isPublic;
        this.phone = phone;
        this.image = image;
    }

}