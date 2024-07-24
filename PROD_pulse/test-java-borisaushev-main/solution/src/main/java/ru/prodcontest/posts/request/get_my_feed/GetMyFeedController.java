package ru.prodcontest.posts.request.get_my_feed;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;
import ru.prodcontest.posts.post.Post;
import ru.prodcontest.profile.me.myProfile.MyProfileService;

import java.util.List;

@RestController
@RequestMapping("/api/posts/feed/my")
public class GetMyFeedController {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private MyProfileService myProfileService;

    @Autowired
    private GetMyFeedService getMyFeedService;

    //проверить токен +
    //получить ленту +
    //спарсить ленту


    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public List<Post> getMyFeed(
            @RequestParam(name = "limit", defaultValue = "5", required = false) int limit,
            @RequestParam(name = "offset", defaultValue = "0", required = false) int offset,
            @RequestHeader(name = "Authorization") String token) {

        token = jwtTokenService.parseToken(token);
        jwtTokenService.validateToken(token);
        myProfileService.validateTokenForUser(token);

        int userId = jwtTokenService.getIdByToken(token);

        List<Post> myFeed = getMyFeedService.getMyFeed(userId, limit, offset);

        System.out.println("\n-------------\nMy feed: ");
        for(var post : myFeed) {
            System.out.println(post);
        }
        System.out.println("-------------\n");

        return myFeed;
    }




}
