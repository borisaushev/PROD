package ru.prodcontest.profiles;

import jakarta.servlet.http.HttpServletResponse;
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
import ru.prodcontest.user.User;

import java.util.Objects;

@RestController
public class UserProfilesController {

    @Autowired
    private UserTableUtil userTableUtil;

    @RequestMapping(method = RequestMethod.GET, path = "/api/profiles/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserProfile(@RequestHeader(name = "Authorization") String unparsedToken,
                                 @PathVariable("login") String login,
                                 HttpServletResponse httpResponse) throws JSONException {

        String token = TokenUtil.parseToken(unparsedToken);

        //Token is invalid
        if (!TokenUtil.isValidToken(token))
            return JsonUtil.getJsonErrorResponse(401, "Переданный токен не существует либо некорректен.",
                    httpResponse);

        String requestLogin = TokenUtil.getLoginByToken(token);

        //No such user
        if (userTableUtil.userDontExists(login))
            return JsonUtil.getJsonErrorResponse(403, "Профиль не может быть получен: " +
                            "пользователь с указанным логином не существует",
                    httpResponse);

        //User is not public
        User user = userTableUtil.getUserByLogin(login);
        if (!user.isPublic && !Objects.equals(requestLogin, login)) {
            boolean isAFriend = false;
            var friendsList = FriendsUtil.getFriendsList(login);
            for (Friend friend : friendsList)
                if (Objects.equals(friend.login(), requestLogin)) {
                    isAFriend = true;
                    break;
                }
            if (!isAFriend)
                return JsonUtil.getJsonErrorResponse(403, "Профиль не может быть получен: " +
                                "у отправителя запроса нет доступа к запрашиваемому профилю",
                        httpResponse);
        }

        JSONObject JsonResponseObject = new JSONObject();
        JsonUtil.fillJsonObjectWithUserData(JsonResponseObject, user);

        return JsonResponseObject.toString();
    }

}
