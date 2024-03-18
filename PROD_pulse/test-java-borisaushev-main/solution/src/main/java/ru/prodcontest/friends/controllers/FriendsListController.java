package ru.prodcontest.friends.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.prodcontest.DataBase.UserTableUtil;
import ru.prodcontest.Json.JsonUtil;
import ru.prodcontest.JwtToken.TokenUtil;
import ru.prodcontest.friends.classes.Friend;
import ru.prodcontest.friends.classes.FriendsUtil;

@RestController
public class FriendsListController {
    @Autowired
    private UserTableUtil userTableUtil;
    @RequestMapping(method = RequestMethod.GET, path = "/api/friends", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getFriendsList(@RequestHeader(name = "Authorization") String unparsedToken,
                                 @RequestParam(name = "limit", required = false) Integer limit,
                                 @RequestParam(name = "offset", required = false) Integer offset,
                            HttpServletResponse httpResponse) throws JSONException {

        String token = TokenUtil.parseToken(unparsedToken);
        //Token is invalid
        if(TokenUtil.isValidToken(token) == false)
            return JsonUtil.getJsonErrorResponse(
                    401, "Переданный токен не существует либо некорректен",
                    httpResponse);

        String login = TokenUtil.getLoginByToken(token);

        if(userTableUtil.userDontExists(login))
            return JsonUtil.getJsonErrorResponse(400,
                    "Пользователь с указанным логином не найден.", httpResponse);

        JSONArray friendsJsonArray = new JSONArray();

        if(limit == null)
            limit = 5;
        if(offset == null)
            offset = 0;

        var friendsList = FriendsUtil.getSortedFriendsList(login);
        for(int i = offset; i < limit && i < friendsList.size(); i++) {
            Friend friend = friendsList.get(i);
            JSONObject friendJson = new JSONObject();
            friendJson.put("login", friend.login());
            friendJson.put("addedAt", friend.addDate());
            friendsJsonArray.put(friendJson);
        }

        return friendsJsonArray.toString();

    }
}
