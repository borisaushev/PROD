package ru.prodcontest.posts.get;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;
import ru.prodcontest.posts.post.Post;
import ru.prodcontest.profile.me.myProfile.MyProfileService;

import java.util.UUID;

@RestController
@RequestMapping("api/posts/{postId}")
public class GetPostController {

    @Autowired
    GetPostRequestService getPostRequestService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private MyProfileService myProfileService;

    @GetMapping
    public Post getPost(@PathVariable UUID postId, @RequestHeader(name = "Authorization") String token) {

        token = jwtTokenService.parseToken(token);
        jwtTokenService.validateToken(token);
        myProfileService.validateTokenForUser(token);

        int userId = jwtTokenService.getIdByToken(token);
        Post post = getPostRequestService.getPost(userId, postId);

        return post;
    }

}
