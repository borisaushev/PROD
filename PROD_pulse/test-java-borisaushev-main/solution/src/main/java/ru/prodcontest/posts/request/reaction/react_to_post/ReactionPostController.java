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

    /*
    * 1. токен
    * 2. доступ
    * 3. последняя реакция
    * 4. меняем цифры как надо
    */


    /*
    * таблица реакций
    * удаляем из таблицы лайков
    * добавляем новую реакцию
    * возвращаем счет(лайк) и счет(дизлайк)
    * */

    @PostMapping("/like")
    public Post likePost(@PathVariable("postId") UUID postId, @RequestHeader(name = "Authorization") String token) {

        token = jwtTokenService.parseToken(token);
        jwtTokenService.validateToken(token);
        myProfileService.validateTokenForUser(token);

        int requestUserId = jwtTokenService.getIdByToken(token);

        Post post = reactionPostService.reactToPost(postId, requestUserId, Reactions.Like);

        return post;
    }

    @PostMapping("/dislike")
    public Post dislikePost(@PathVariable("postId") UUID postId, @RequestHeader(name = "Authorization") String token) {

        token = jwtTokenService.parseToken(token);
        jwtTokenService.validateToken(token);
        myProfileService.validateTokenForUser(token);

        int requestUserId = jwtTokenService.getIdByToken(token);

        Post post = reactionPostService.reactToPost(postId, requestUserId, Reactions.Dislike);

        return post;
    }

}
