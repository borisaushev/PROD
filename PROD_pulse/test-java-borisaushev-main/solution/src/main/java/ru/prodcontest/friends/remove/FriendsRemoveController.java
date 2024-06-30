package ru.prodcontest.friends.remove;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;
import ru.prodcontest.profile.me.myProfile.MyProfileService;
/*

1. валидация токена +
2. validate login +
3. Remove

*/

@RestController
@RequestMapping("api/friends/remove")
public class FriendsRemoveController {


    @Autowired
    private MyProfileService myProfileService;
    @Autowired
    private FriendRemoveService friendRemoveService;
    @Autowired
    private JwtTokenService jwtTokenService;


    @PostMapping
    @ResponseBody
    public String removeFriend(@RequestBody String userData, @RequestHeader(name = "Authorization") String token) throws JSONException {

        System.out.println("--------------------\nRemoveFriend1");

        JSONObject json = new JSONObject(userData);
        String login = json.getString("login");

        System.out.println("RemoveFriend1");

        token = jwtTokenService.parseToken(token);
        jwtTokenService.validateToken(token);
        myProfileService.validateTokenForUser(token);

        System.out.println("RemoveFriend2");

        friendRemoveService.removeFriend(login, token);

        System.out.println("RemoveFriend3");

        return """
                {
                  "status": "ok"
                }
                """;

    }
}
