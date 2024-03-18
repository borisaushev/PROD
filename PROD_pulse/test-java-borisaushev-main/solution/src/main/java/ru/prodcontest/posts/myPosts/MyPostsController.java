package ru.prodcontest.posts.myPosts;

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
import ru.prodcontest.posts.classes.Post;
import ru.prodcontest.posts.utils.PostsUtil;

@RestController
public class MyPostsController {
    @Autowired
    private UserTableUtil userTableUtil;
    @RequestMapping(method = RequestMethod.GET, path = "/api/posts/feed/my", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getMyPosts(@RequestHeader(name = "Authorization") String unparsedToken,
                                 @RequestParam(name = "limit", required = false) Integer limit,
                                 @RequestParam(name = "offset", required = false) Integer offset,
                                 HttpServletResponse httpResponse) throws JSONException {

        String token = TokenUtil.parseToken(unparsedToken);
        //Token is invalid
        if(TokenUtil.isValidToken(token) == false)
            return JsonUtil.getJsonErrorResponse(
                    401, "Переданный токен не существует либо некорректен",
                    httpResponse);

        String login = TokenUtil.getLoginByToken(token);

        if(userTableUtil.userDontExists(login))
            return JsonUtil.getJsonErrorResponse(400,
                    "Пользователь с указанным логином не найден.", httpResponse);

        JSONArray postsJsonArray = new JSONArray();

        if(limit == null)
            limit = 5;
        if(offset == null)
            offset = 0;

        var myPostsList = PostsUtil.getSortedPostsList(login);
        for(int i = offset; i < limit && i < myPostsList.size(); i++) {
            Post post = myPostsList.get(i);
            JSONObject postJson = PostsUtil.getPostJsonObject(post);
            postsJsonArray.put(postJson);
        }

        return postsJsonArray.toString();

    }
}
