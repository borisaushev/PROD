package ru.prodcontest.posts.request.get_ones_feed;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;
import ru.prodcontest.posts.post.Post;
import ru.prodcontest.profile.me.myProfile.MyProfileService;

import java.util.List;

@RestController
@RequestMapping("/api/posts/feed/{login}")
public class GetOnesFeedController {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private MyProfileService myProfileService;

    @Autowired
    private GetOnesFeedService getOnesFeedService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public List<Post> getOnesFeed(
            @PathVariable("login") String login,
            @RequestParam(name = "limit", defaultValue = "5", required = false) int limit,
            @RequestParam(name = "offset", defaultValue = "0", required = false) int offset,
            @RequestHeader(name = "Authorization") String token) {

        token = jwtTokenService.parseToken(token);
        jwtTokenService.validateToken(token);
        myProfileService.validateTokenForUser(token);

        int requestUserId = jwtTokenService.getIdByToken(token);
        List<Post> onesFeed = getOnesFeedService.getOnesFeed(login, requestUserId, limit, offset);

        System.out.println("\n-------------\n" + login + "'s feed: ");
        for(var post : onesFeed) {
            System.out.println(post);
        }
        System.out.println("-------------\n");

        return onesFeed;
    }




}
