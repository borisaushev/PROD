package ru.prodcontest.posts.request.reaction.react_to_post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;
import ru.prodcontest.posts.post.Post;
import ru.prodcontest.posts.request.reaction.Reactions;
import ru.prodcontest.profile.me.myProfile.MyProfileService;

import java.util.UUID;

@RestController
@RequestMapping("/api/posts/{postId}/")
public class ReactionPostController {

    @Autowired
    ReactionPostService reactionPostService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private MyProfileService myProfileService;

    @PostMapping("/like")
    public Post likePost(@PathVariable("postId") UUID postId, @RequestHeader(name = "Authorization") String token) {
        System.out.println("Reacting to post: " + postId);

        token = validateToken(token);

        int requestUserId = jwtTokenService.getIdByToken(token);

        Post post = reactionPostService.reactToPost(postId, requestUserId, Reactions.Like);

        System.out.println("After reaction: " + post);

        return post;
    }

    @PostMapping("/dislike")
    public Post dislikePost(@PathVariable("postId") UUID postId, @RequestHeader(name = "Authorization") String token) {

        System.out.println("Reacting to post: " + postId);

        token = validateToken(token);

        int requestUserId = jwtTokenService.getIdByToken(token);

        Post post = reactionPostService.reactToPost(postId, requestUserId, Reactions.Dislike);

        System.out.println("After reaction: " + post);

        return post;
    }

    private String validateToken(String token) {
        token = jwtTokenService.parseToken(token);
        jwtTokenService.validateToken(token);
        myProfileService.validateTokenForUser(token);
        return token;
    }

}
