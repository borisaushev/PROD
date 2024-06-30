package ru.prodcontest.friends.add;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;
import ru.prodcontest.profile.me.myProfile.MyProfileService;
/*

1. валидация токена +
2. validate login +
3. add

*/

@RestController
@RequestMapping("api/friends/add")
public class FriendsAddController {


    @Autowired
    private MyProfileService myProfileService;
    @Autowired
    private FriendAddService friendAddService;
    @Autowired
    private JwtTokenService jwtTokenService;


    @PostMapping
    @ResponseBody
    public String addFriend(@RequestBody String userData, @RequestHeader(name = "Authorization") String token) throws JSONException {

        System.out.println("--------------------\naddFriend1");

        JSONObject json = new JSONObject(userData);
        String login = json.getString("login");

        System.out.println("addFriend1");

        token = jwtTokenService.parseToken(token);
        jwtTokenService.validateToken(token);
        myProfileService.validateTokenForUser(token);

        System.out.println("addFriend2");

        friendAddService.validateLogin(login);

        friendAddService.addFriend(login, token);

        System.out.println("addFriend3");

        return """
                {
                  "status": "ok"
                }
                """;

    }
}
