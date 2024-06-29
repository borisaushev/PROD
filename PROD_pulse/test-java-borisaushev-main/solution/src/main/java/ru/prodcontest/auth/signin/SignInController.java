package ru.prodcontest.auth.signin;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth/sign-in")
public class SignInController {

    @Autowired
    SignInService signInService;

    @PostMapping
    @ResponseBody
    public String signIn(@RequestBody String userData) throws JSONException {
        JSONObject JsonUserData = new JSONObject(userData);

        String login = JsonUserData.getString("login");
        String password = JsonUserData.getString("password");

        signInService.validateUserData(login, password);

        String token = signInService.getToken(login, password);

        signInService.saveTokenForUser(token);

        var jsonResponse = new JSONObject();
        jsonResponse.put("token", token);

        return jsonResponse.toString();

    }
}
