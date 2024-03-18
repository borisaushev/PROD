package ru.prodcontest.posts.getPostsByLogin;


import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
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
public class GetPostsByLogin {

    @Autowired
    private UserTableUtil userTableUtil;

    @RequestMapping(method = RequestMethod.GET, path="/api/posts/feed/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPost(@RequestHeader(name = "Authorization") String unparsedToken,
                          @PathVariable("login") String author,
                          @RequestParam(name = "limit", required = false) Integer limit,
                          @RequestParam(name = "offset", required = false) Integer offset,
                          HttpServletResponse httpResponse) throws JSONException {

        String token = TokenUtil.parseToken(unparsedToken);

        //Token is invalid
        if(TokenUtil.isValidToken(token) == false)
            return JsonUtil.getJsonErrorResponse(401, "Переданный токен не существует либо некорректен.",
                    httpResponse);

        String requestLogin = TokenUtil.getLoginByToken(token);

        //No such user
        if(userTableUtil.userDontExists(author))
            return JsonUtil.getJsonErrorResponse(404, "Пользователь не найден",
                    httpResponse);

        //User is not public
        User user = userTableUtil.getUserByLogin(author);
        if(user.isPublic == false && !Objects.equals(requestLogin, author)) {
            boolean isAFriend = false;

            var friendsList = FriendsUtil.getFriendsList(author);
            for(Friend friend : friendsList)
                if(Objects.equals(friend.login(), requestLogin))
                    isAFriend = true;

            if(isAFriend == false)
                return JsonUtil.getJsonErrorResponse(404, "Пользователь не найден либо к нему нет доступа.",
                        httpResponse);
        }

        JSONArray postsJsonArray = new JSONArray();

        if(limit == null)
            limit = 5;
        if(offset == null)
            offset = 0;

        var postsList = PostsUtil.getSortedPostsList(author);
        for(int i = offset; i < limit && i < postsList.size(); i++) {
            Post post = postsList.get(i);
            JSONObject postJson = PostsUtil.getPostJsonObject(post);
            postsJsonArray.put(postJson);
        }

        return postsJsonArray.toString();
    }

}