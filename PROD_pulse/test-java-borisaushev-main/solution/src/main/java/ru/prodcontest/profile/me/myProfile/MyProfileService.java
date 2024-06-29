package ru.prodcontest.profile.me.myProfile;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.prodcontest.auth.signin.token.Exceptions.TokenDoesntExistException;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;
import ru.prodcontest.userInfo.Exceptions.UserAlreadyExistsException;
import ru.prodcontest.userInfo.User;
import ru.prodcontest.userInfo.UserDataUtil;
import ru.prodcontest.userInfo.repository.UserRepository;

import java.util.InputMismatchException;
import java.util.List;

@Service
public class MyProfileService {
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private UserRepository userRepository;
    public void validateTokenForUser(String token) {
        int id = jwtTokenService.getIdByToken(token);

        List<String> validTokens = userRepository.getValidTokensById(id);
        for(String validToken : validTokens)
            if(validToken.equals(token))
                return;

        throw new TokenDoesntExistException("token doesnt exists");
    }

    public User getUserByToken(String token) {
        int id = jwtTokenService.getIdByToken(token);
        User user = userRepository.getUserById(id);
        return user;
    }
    public void validateUserData(JSONObject JsonUserData, String token) throws JSONException {
        validateUserInfo(JsonUserData);
        validateUserDataOnCollisions(JsonUserData, token);
    }
    public void validateUserInfo(JSONObject JsonUserData) throws JSONException {
        boolean isValid = true;
        if (JsonUserData.has("login"))
            isValid &= UserDataUtil.validateUserLogin(JsonUserData.getString("login"));

        if (JsonUserData.has("email"))
            isValid &= UserDataUtil.validateUserEmail(JsonUserData.getString("email"));

        if (JsonUserData.has("countryCode"))
            isValid &= UserDataUtil.validateUserCountryCode(JsonUserData.getString("countryCode"));

        if (JsonUserData.has("phone"))
            isValid &= UserDataUtil.validateUserPhone(JsonUserData.getString("phone"));

        if (JsonUserData.has("image"))
            isValid &= UserDataUtil.validateUserImage(JsonUserData.getString("image"));

        if(!isValid)
            throw new InputMismatchException("неверные данные");

    }
    public void validateUserDataOnCollisions(JSONObject JsonUserData, String token) throws JSONException {
        int userId = jwtTokenService.getIdByToken(token);
        if (JsonUserData.has("phone"))
            if(userRepository.userExistsWithProperty("user_phone", JsonUserData.getString("phone"))) {
                if(Boolean.FALSE == userRepository.userExistsWithProperties(new String[]{"user_phone", "id"}, new Object[]{JsonUserData.getString("phone"), userId}))
                    throw new UserAlreadyExistsException("пользователь с таким номером телефона уже существеет");
            }
    }


        public void updateUserData(String token, JSONObject JsonUserData) throws JSONException {
        int id = jwtTokenService.getIdByToken(token);

        if (JsonUserData.has("login"))
            userRepository.updateUserProperty(id, "login", JsonUserData.getString("login"));

        if (JsonUserData.has("email"))
            userRepository.updateUserProperty(id, "user_email", JsonUserData.getString("email"));

        if (JsonUserData.has("countryCode"))
            userRepository.updateUserProperty(id, "country_code", JsonUserData.getString("countryCode"));

        if (JsonUserData.has("isPublic"))
            userRepository.updateUserProperty(id, "is_public", JsonUserData.getBoolean("isPublic"));

        if (JsonUserData.has("phone"))
            userRepository.updateUserProperty(id, "user_phone", JsonUserData.getString("phone"));

        if (JsonUserData.has("image"))
            userRepository.updateUserProperty(id, "user_image", JsonUserData.getString("image"));
    }
    
}
