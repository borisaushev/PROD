package ru.prodcontest.auth.signin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.prodcontest.auth.signin.Exceptions.UserDoesntExistsException;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;
import ru.prodcontest.user.UserDataUtil;
import ru.prodcontest.user.repository.UserRepository;

import java.util.HashMap;

@Service
public class SignInService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenService jwtTokenService;

    public void validateUserData(String login, String password) {
        String hashedPassword = UserDataUtil.hashPassword(password);
        if(userRepository.userExists(login, hashedPassword) == Boolean.FALSE)
            throw new UserDoesntExistsException("no such user");
    }

    public String getToken(String login, String password) {
        int userId = getUserId(login, password);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("userId", userId);

        return jwtTokenService.generateToken(properties);

    }

    private int getUserId(String login, String password) {
        String hashedPassword = UserDataUtil.hashPassword(password);
        return userRepository.getUserId(login, hashedPassword);
    }

    public void saveTokenForUser(String token) {
        int userId = jwtTokenService.getClaims(token).get("userId", Integer.class);
        userRepository.saveTokenForUser(userId, token);
    }

}
