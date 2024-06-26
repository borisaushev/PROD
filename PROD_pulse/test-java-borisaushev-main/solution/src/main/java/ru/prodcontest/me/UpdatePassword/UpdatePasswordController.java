package ru.prodcontest.me.UpdatePassword;

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

import java.util.Objects;

@RestController
public class UpdatePasswordController {
    @Autowired
    private UserTableUtil userTableUtil;

    @RequestMapping(method = RequestMethod.POST, path = "/api/me/updatePassword", produces = MediaType.APPLICATION_JSON_VALUE)
    public String updatePassword(@RequestBody String userData,
                                 @RequestHeader(name = "Authorization") String unparsedToken,
                                 HttpServletResponse httpResponse) throws JSONException {

        //parsing user data
        JSONObject newUserData = new JSONObject(userData);
        String token = TokenUtil.parseToken(unparsedToken);

        if (!newUserData.has("oldPassword") || !newUserData.has("newPassword"))
            return JsonUtil.getJsonErrorResponse(
                    400, "Некорректные данные",
                    httpResponse);

        //Token is invalid
        if (!TokenUtil.isValidToken(token))
            return JsonUtil.getJsonErrorResponse(
                    401, "Переданный токен не существует либо некорректен",
                    httpResponse);

        String login = TokenUtil.getLoginByToken(token);
        String oldPassword = TokenUtil.getPasswordByToken(token);

        if (!Objects.equals(oldPassword, newUserData.getString("oldPassword")))
            return JsonUtil.getJsonErrorResponse(
                    403, "Указанный пароль не совпадает с действительным",
                    httpResponse);

        //no such user
        if (login == null || !userTableUtil.userExists(login, oldPassword))
            return JsonUtil.getJsonErrorResponse(
                    400, "Пользователя не существует",
                    httpResponse);

        //getting old user data
        User user = userTableUtil.getUserByLogin(login);
        //updating it
        user.setPassword(newUserData.getString("newPassword"));

        if (UserDataUtil.validateUserData(user))
            return JsonUtil.getJsonErrorResponse(
                    400, "Данные не соответствуют ожидаемому формату и требованиям",
                    httpResponse);

        userTableUtil.updateUserInfo(login, user);

        TokenUtil.recallAllTokens(login);
        TokenUtil.updateLoginMap(login, newUserData.getString("newPassword"));

        JSONObject JsonResponseObject = new JSONObject();
        JsonResponseObject.put("status", "ok");

        return JsonResponseObject.toString();
    }
}