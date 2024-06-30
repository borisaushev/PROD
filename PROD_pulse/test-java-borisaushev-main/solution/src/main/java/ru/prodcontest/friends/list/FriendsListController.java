package ru.prodcontest.friends.list;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.web.bind.annotation.*;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;
import ru.prodcontest.friends.list.FriendsListService;
import ru.prodcontest.profile.me.myProfile.MyProfileService;

@RestController
@RequestMapping("api/friends")
public class FriendsListController {


    @Autowired
    private MyProfileService myProfileService;
    @Autowired
    private FriendsListService friendListService;
    @Autowired
    private JwtTokenService jwtTokenService;


    @GetMapping
    @ResponseBody
    public String ListFriends(@RequestHeader(name = "Authorization") String token,
                              @RequestParam(name = "limit", defaultValue = "5", required = false) int limit,
                              @RequestParam(name = "offset", defaultValue = "0", required = false) int offset
                              ) throws JSONException {

        System.out.println("--------------------\nListFriend1");

        token = jwtTokenService.parseToken(token);
        jwtTokenService.validateToken(token);
        myProfileService.validateTokenForUser(token);

        System.out.println("ListFriend2");

        var friends = friendListService.getFriendList(token, limit, offset);

        System.out.println("ListFriend3");

        var result = friendListService.parseFriendList(friends).toString();

        System.out.println("ListFriend4");

        return result;

    }
}