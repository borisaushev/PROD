package ru.prodcontest.me.Profile;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.prodcontest.DataBase.UserTableUtil;
import ru.prodcontest.Json.JsonUtil;
import ru.prodcontest.JwtToken.TokenUtil;
import ru.prodcontest.user.User;

@RestController
public class ProfileController {

    @Autowired
    private UserTableUtil userTableUtil;

    @RequestMapping(method = RequestMethod.GET, path = "/api/me/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserProfile(@RequestHeader(name = "Authorization") String unparsedToken,
                                 HttpServletResponse httpResponse) throws JSONException {

        String token = TokenUtil.parseToken(unparsedToken);

        //Token is invalid
        if (!TokenUtil.isValidToken(token))
            return JsonUtil.getJsonErrorResponse(
                    401, "Переданный токен не существует либо некорректен",
                    httpResponse);

        String login = TokenUtil.getLoginByToken(token);

        if (login == null)
            return JsonUtil.getJsonErrorResponse(400, "Переданный токен не существует либо некорректен.",
                    httpResponse);

        User user = userTableUtil.getUserByLogin(login);
        JSONObject JsonResponseObject = new JSONObject();
        JsonUtil.fillJsonObjectWithUserData(JsonResponseObject, user);

        return JsonResponseObject.toString();
    }

}
