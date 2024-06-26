package ru.prodcontest.posts.getPostById;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.prodcontest.DataBase.UserTableUtil;
import ru.prodcontest.Json.JsonUtil;
import ru.prodcontest.JwtToken.TokenUtil;
import ru.prodcontest.friends.classes.Friend;
import ru.prodcontest.friends.classes.FriendsUtil;
import ru.prodcontest.posts.classes.Post;
import ru.prodcontest.posts.utils.PostsUtil;
import ru.prodcontest.user.User;

import java.util.Objects;

@RestController
public class GetPostController {

    @Autowired
    private UserTableUtil userTableUtil;

    @RequestMapping(method = RequestMethod.GET, path = "/api/posts/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPost(@RequestHeader(name = "Authorization") String unparsedToken,
                          @PathVariable("postId") String postId,
                          HttpServletResponse httpResponse) throws JSONException {

        String token = TokenUtil.parseToken(unparsedToken);

        //Token is invalid
        if (!TokenUtil.isValidToken(token))
            return JsonUtil.getJsonErrorResponse(401, "Переданный токен не существует либо некорректен.",
                    httpResponse);

        String requestLogin = TokenUtil.getLoginByToken(token);

        Post post = PostsUtil.getPostByPostId(postId);
        if (post == null)
            return JsonUtil.getJsonErrorResponse(404, "Указанный пост не найден либо к нему нет доступа.",
                    httpResponse);

        String author = post.author();

        //No such user
        if (userTableUtil.userDontExists(author))
            return JsonUtil.getJsonErrorResponse(400, "Автор не найден",
                    httpResponse);

        //User is not public
        User user = userTableUtil.getUserByLogin(author);
        if (!user.isPublic && !Objects.equals(requestLogin, author)) {
            boolean isAFriend = false;
            var friendsList = FriendsUtil.getFriendsList(author);
            for (Friend friend : friendsList)
                if (Objects.equals(friend.login(), requestLogin)) {
                    isAFriend = true;
                    break;
                }
            if (!isAFriend)
                return JsonUtil.getJsonErrorResponse(404, "Указанный пост не найден либо к нему нет доступа.",
                        httpResponse);
        }

        JSONObject JsonResponseObject = new JSONObject();
        JsonResponseObject.put("id", post.id());
        JsonResponseObject.put("content", post.content());
        JsonResponseObject.put("tags", post.tags());
        JsonResponseObject.put("createdAt", post.createdAt());
        JsonResponseObject.put("likesCount", post.likesCount());
        JsonResponseObject.put("dislikesCount", post.dislikesCount());

        return JsonResponseObject.toString();
    }

}