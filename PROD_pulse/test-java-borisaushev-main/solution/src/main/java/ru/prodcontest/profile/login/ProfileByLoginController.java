package ru.prodcontest.profile.login;

/*
0. create table friends (id, friend_with(ref on id(users)), added_at)

1. валидация токена +
2. validate login +
3. check if users are friends or user is himself or profile is public +
4. return profile +
*/

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.prodcontest.Json.JsonUtil;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;
import ru.prodcontest.profile.me.myProfile.MyProfileService;
import ru.prodcontest.userInfo.User;

@RestController
@RequestMapping("api/profiles/")
public class ProfileByLoginController {


    @Autowired
    private MyProfileService myProfileService;
    @Autowired
    private ProfileByLoginService loginService;
    @Autowired
    private JwtTokenService jwtTokenService;


    @GetMapping("{login}")
    @ResponseBody
    public String getProfileByLogin(@PathVariable String login, @RequestHeader(name = "Authorization") String token) throws JSONException {

        System.out.println("--------------------\nallGood");
        token = jwtTokenService.parseToken(token);
        jwtTokenService.validateToken(token);
        myProfileService.validateTokenForUser(token);

        System.out.println("allGood");

        loginService.validateLogin(login);

        loginService.checkIfUserProfileIsAvailable(login, token);

        System.out.println("allGood");

        User profile = loginService.getRequestedUserProfile(login);

        JSONObject result = JsonUtil.getUserJson(profile).getJSONObject("profile");
        System.out.println("allGood");
        return result.toString();

    }
}
