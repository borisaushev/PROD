package ru.prodcontest.auth.sign_in;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.prodcontest.DataBase.UserTableUtil;
import ru.prodcontest.Json.JsonUtil;
import ru.prodcontest.JwtToken.TokenUtil;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@RestController
@ComponentScan()
public class UserSignInController {
    @Autowired
    private UserTableUtil userTableUtil;

    @RequestMapping(method = RequestMethod.POST, path = "/api/auth/sign-in", produces = MediaType.APPLICATION_JSON_VALUE)
    public String signInUser(@RequestBody String userData, HttpServletResponse httpResponse) throws JSONException, SQLException, UnsupportedEncodingException {

        //parsing user data
        JSONObject JsonUserData = new JSONObject(userData);

        if (!JsonUserData.has("login") || !JsonUserData.has("password"))
            return JsonUtil.getJsonErrorResponse(400,
                    "Неправильный формат данных", httpResponse);

        String login = JsonUserData.getString("login");
        String password = JsonUserData.getString("password");

        //If user not found returning error message
        if (!userTableUtil.userExists(login, password))
            return JsonUtil.getJsonErrorResponse(401,
                    "Пользователь с указанным логином и паролем не найден", httpResponse);

        //creating jwt token
        String jwtToken = TokenUtil.createJWTToken();

        JSONObject JsonResponseObject = new JSONObject();
        JsonResponseObject.put("token", jwtToken);

        TokenUtil.updateTokenMap(jwtToken, login);
        TokenUtil.updateLoginMap(login, password);

        return JsonResponseObject.toString();
    }

}
