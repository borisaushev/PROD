package ru.prodcontest.me.Profile;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.prodcontest.DataBase.UserTableUtil;
import ru.prodcontest.Json.JsonUtil;
import ru.prodcontest.JwtToken.TokenUtil;
import ru.prodcontest.user.User;
import ru.prodcontest.user.UserDataUtil;

@RestController
public class UpdateProfileController {
    @Autowired
    private UserTableUtil userTableUtil;

    @RequestMapping(method = RequestMethod.PATCH, path = "/api/me/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserProfile(@RequestBody String userData,
                                 @RequestHeader(name = "Authorization") String unparsedToken,
                                 HttpServletResponse httpResponse) throws JSONException {

        //parsing user data
        JSONObject newUserData = new JSONObject(userData);
        String token = TokenUtil.parseToken(unparsedToken);

        //Token is invalid
        if (!TokenUtil.isValidToken(token))
            return JsonUtil.getJsonErrorResponse(
                    401, "Переданный токен не существует либо некорректен",
                    httpResponse);

        String login = TokenUtil.getLoginByToken(token);
        String oldPassword = TokenUtil.getPasswordByToken(token);
        newUserData.put("password", oldPassword);

        //no such user
        if (userTableUtil.userDontExists(login))
            return JsonUtil.getJsonErrorResponse(
                    401, "Пользователя не существует",
                    httpResponse);

        //getting old user data
        User user = userTableUtil.getUserByLogin(login);
        //updating it
        updateUserData(newUserData, user);

        if (UserDataUtil.validateUserData(user) || newUserData.has("login") || newUserData.has("password"))
            return JsonUtil.getJsonErrorResponse(
                    400, "Данные не соответствуют ожидаемому формату и требованиям",
                    httpResponse);

        if (newUserData.has("phone") && userTableUtil.phoneNumberAlreadyTaken(user.phone))
            return JsonUtil.getJsonErrorResponse(
                    409, "Нарушено требование на уникальность авторизационных данных пользователей",
                    httpResponse);

        userTableUtil.updateUserInfo(login, user);

        JSONObject JsonResponseObject = new JSONObject();
        JsonUtil.fillJsonObjectWithUserData(JsonResponseObject, user);

        return JsonResponseObject.toString();
    }

    private void updateUserData(JSONObject newUserData, User user) throws JSONException {
        if (newUserData.has("login"))
            user.setLogin(newUserData.getString("login"));

        if (newUserData.has("email"))
            user.setEmail(newUserData.getString("email"));

        if (newUserData.has("password"))
            user.setPassword(newUserData.getString("password"));

        if (newUserData.has("countryCode"))
            user.setCountryCode(newUserData.getString("countryCode"));

        if (newUserData.has("isPublic"))
            user.setPublic(newUserData.getBoolean("isPublic"));

        if (newUserData.has("phone"))
            user.setPhone(newUserData.getString("phone"));

        if (newUserData.has("image"))
            user.setImage(newUserData.getString("image"));

    }

}