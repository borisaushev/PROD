package ru.prodcontest.posts.publish;

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
import ru.prodcontest.posts.utils.RandomString;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class PostPublishController {
    @Autowired
    private UserTableUtil userTableUtil;

    @RequestMapping(method = RequestMethod.POST, path = "/api/posts/new", produces = MediaType.APPLICATION_JSON_VALUE)
    public String publishPost(@RequestBody String postData,
                              @RequestHeader(name = "Authorization") String unparsedToken,
                              HttpServletResponse httpResponse) throws JSONException {

        String token = TokenUtil.parseToken(unparsedToken);
        //Token is invalid
        if (!TokenUtil.isValidToken(token))
            return JsonUtil.getJsonErrorResponse(
                    401, "Переданный токен не существует либо некорректен",
                    httpResponse);

        //parsing post data
        JSONObject JsonPostData = new JSONObject(postData);

        if (!JsonPostData.has("content") || !JsonPostData.has("tags"))
            return JsonUtil.getJsonErrorResponse(400,
                    "Неправильный формат данных", httpResponse);

        String content = JsonPostData.getString("content");
        String author = TokenUtil.getLoginByToken(token);
        JSONArray tags = JsonPostData.getJSONArray("tags");

        RandomString gen = new RandomString(12, ThreadLocalRandom.current());
        String randomId = gen.nextString();
        Date createdAt = Date.from(Instant.now());
        int dislikes = 0, likes = 0;

        Post post = new Post(randomId, content, author, tags, createdAt, likes, dislikes);
        PostsUtil.addPost(author, post);

        JSONObject postJsonObject = PostsUtil.getPostJsonObject(post);

        return postJsonObject.toString();

    }
}
