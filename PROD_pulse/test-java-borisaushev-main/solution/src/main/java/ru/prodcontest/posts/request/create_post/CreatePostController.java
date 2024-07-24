package ru.prodcontest.posts.request.create_post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;
import ru.prodcontest.posts.post.Post;
import ru.prodcontest.posts.post.request_object.CreatePostRequestObject;
import ru.prodcontest.profile.me.myProfile.MyProfileService;

@RestController
@RequestMapping("api/posts/new")
public class CreatePostController {

    @Autowired
    private CreatePostService createPostService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private MyProfileService myProfileService;


    @PostMapping
    public Post createPost(@RequestBody CreatePostRequestObject postInfo, @RequestHeader(name = "Authorization") String token) {

        token = jwtTokenService.parseToken(token);
        jwtTokenService.validateToken(token);
        myProfileService.validateTokenForUser(token);

        int userId = jwtTokenService.getIdByToken(token);
        Post post = createPostService.savePost(postInfo, userId);

        System.out.println(post);

        return post;
    }



}
