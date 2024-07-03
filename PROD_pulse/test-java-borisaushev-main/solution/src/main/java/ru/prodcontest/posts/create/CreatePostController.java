package ru.prodcontest.posts.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;
import ru.prodcontest.profile.me.myProfile.MyProfileService;

@RestController
@RequestMapping("api/posts/new")
public class CreatePostController {
    //TODO: сделать:
    /*
    0.0. create model on repository, controller, service, exc handler +

    1. create posts table
        author_id, content, TODO: tags, created_at, likes_count, dislike_count +
    2. validate token
    3.1. create posts repository, classes and parsers
    3.2. save post
    4. return post

    */

    @Autowired
    private CreatePostService service;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private MyProfileService myProfileService;

    @PostMapping
    @ResponseBody
    public String createPost(@RequestBody CreatePostRequestObject postInfo, @RequestHeader(name = "Authorization") String token) {

        token = jwtTokenService.parseToken(token);
        jwtTokenService.validateToken(token);
        myProfileService.validateTokenForUser(token);

        System.out.println(postInfo);

        return "";
    }



}
