package ru.prodcontest.profile.me.myProfile;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.prodcontest.Json.JsonUtil;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;
import ru.prodcontest.userInfo.User;

@RestController
@RequestMapping("/api/me/profile")
@ResponseStatus(HttpStatus.OK)
public class MyProfileController {

    @Autowired
    private MyProfileService myProfileService;
    @Autowired
    private JwtTokenService jwtTokenService;

    @GetMapping
    @ResponseBody
    public String getMyProfile(@RequestHeader(name = "Authorization") String token) throws JSONException {

        token = jwtTokenService.parseToken(token);
        jwtTokenService.validateToken(token);
        myProfileService.validateTokenForUser(token);

        User user = myProfileService.getUserByToken(token);
        var resultJson = JsonUtil.getUserJson(user);

        resultJson = resultJson.getJSONObject("profile");

        System.out.println(resultJson);

        return resultJson.toString();

    }

    @PatchMapping
    @ResponseBody
    public String patchMyProfile(@RequestHeader(name = "Authorization") String token,
                                 @RequestBody String userData) throws JSONException {

        token = jwtTokenService.parseToken(token);
        jwtTokenService.validateToken(token);
        myProfileService.validateTokenForUser(token);

        JSONObject JsonUserData = new JSONObject(userData);
        myProfileService.validateUserData(JsonUserData, token);
        myProfileService.updateUserData(token, JsonUserData);

        User user = myProfileService.getUserByToken(token);
        var resultJson = JsonUtil.getUserJson(user);

        resultJson = resultJson.getJSONObject("profile");

        System.out.println(resultJson);

        return resultJson.toString();

    }


    }
