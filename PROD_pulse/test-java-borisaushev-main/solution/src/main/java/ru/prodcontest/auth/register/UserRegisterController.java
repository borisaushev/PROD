package ru.prodcontest.auth.register;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.prodcontest.Json.JsonUtil;
import ru.prodcontest.user.User;
import ru.prodcontest.user.UserDataUtil;

@RequestMapping("api/auth/register")
@RestController
public class UserRegisterController {
    @Autowired
    UserService userService;

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public String registerUser(@RequestBody String userData) throws JSONException {
        JSONObject JsonUserData = new JSONObject(userData);

        User user = UserDataUtil.parseUserData(JsonUserData);

        UserDataUtil.validateUserData(user);

        userService.checkIfUserAlreadyExists(user);

        userService.saveUser(user);

        System.out.println("register: " + JsonUtil.getUserJson(user));

        return JsonUtil.getUserJson(user).toString();

    }
}
