package ru.prodcontest.auth.register;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.prodcontest.DataBase.UserTableUtil;
import ru.prodcontest.Json.JsonUtil;
import ru.prodcontest.user.User;
import ru.prodcontest.user.UserDataUtil;

import java.sql.SQLException;

@RestController
public class UserRegisterController {

    @Autowired
    private UserTableUtil userTableUtil;

    @GetMapping("/api/auth/register")
    @RequestMapping(method = RequestMethod.POST, path = "/api/auth/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public String registerUser(@RequestBody String userData, HttpServletResponse httpResponse) throws JSONException, SQLException {

        //create Table If Needed
        userTableUtil.createTableIfNeeded();

        //parsing user data
        JSONObject JsonUserData = new JSONObject(userData);
        User user = UserDataUtil.parseUserData(JsonUserData);

        //validateUserData
        if (UserDataUtil.validateUserData(user))
            return JsonUtil.getJsonErrorResponse(400,
                    "Регистрационные данные не соответствуют ожидаемому формату и требованиям", httpResponse);

            //if userAlreadyExists
        else if (userTableUtil.userExists(user))
            return JsonUtil.getJsonErrorResponse(409,
                    "Нарушено требование на уникальность авторизационных данных пользователей", httpResponse);

        //all clear, can save user now
        userTableUtil.saveUser(user);

        JsonUserData.remove("password");
        JSONObject JsonResponse = new JSONObject();
        JsonResponse.put("profile", JsonUserData);

        httpResponse.setStatus(201);
        return JsonResponse.toString();

    }


}
