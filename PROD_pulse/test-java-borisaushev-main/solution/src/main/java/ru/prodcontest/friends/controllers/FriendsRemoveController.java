package ru.prodcontest.friends.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.prodcontest.DataBase.UserTableUtil;
import ru.prodcontest.Json.JsonUtil;
import ru.prodcontest.JwtToken.TokenUtil;
import ru.prodcontest.friends.classes.FriendsUtil;

@RestController
public class FriendsRemoveController {
    @Autowired
    private UserTableUtil userTableUtil;
    @RequestMapping(method = RequestMethod.POST, path = "/api/friends/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public String removeFriend(@RequestBody String friendData,
                            @RequestHeader(name = "Authorization") String unparsedToken,
                            HttpServletResponse httpResponse) throws JSONException {

        String token = TokenUtil.parseToken(unparsedToken);
        //Token is invalid
        if(TokenUtil.isValidToken(token) == false)
            return JsonUtil.getJsonErrorResponse(
                    401, "Переданный токен не существует либо некорректен",
                    httpResponse);

        //parsing user data
        JSONObject JsonUserData = new JSONObject(friendData);

        if(JsonUserData.has("login") == false)
            return JsonUtil.getJsonErrorResponse(400,
                    "Неправильный формат данных", httpResponse);

        String friendLogin = JsonUserData.getString("login");
        String login = TokenUtil.getLoginByToken(token);

        if(userTableUtil.userDontExists(friendLogin) || userTableUtil.userDontExists(login))
            return JsonUtil.getJsonErrorResponse(404,
                    "Пользователь с указанным логином не найден.", httpResponse);

        FriendsUtil.removeFriend(login, friendLogin);

        JSONObject JsonResponseObject = new JSONObject();
        JsonResponseObject.put("status", "ok");

        return JsonResponseObject.toString();

    }
}