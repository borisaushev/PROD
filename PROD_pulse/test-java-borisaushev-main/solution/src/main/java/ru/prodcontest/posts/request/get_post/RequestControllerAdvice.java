package ru.prodcontest.posts.request.get_post;


import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.prodcontest.Json.JsonUtil;
import ru.prodcontest.posts.post.exception.PostIsPrivateException;
import ru.prodcontest.posts.post.exception.PostNotFoundException;

@ControllerAdvice("ru.prodcontest.posts")
@ResponseBody
public class RequestControllerAdvice {

    @ExceptionHandler(PostIsPrivateException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String postIsPrivate(PostIsPrivateException exc, HttpServletResponse response) throws JSONException {
        return JsonUtil.getJsonErrorResponse(404, exc.getMessage(), response);
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String postIsPrivate(PostNotFoundException exc, HttpServletResponse response) throws JSONException {
        return JsonUtil.getJsonErrorResponse(404, exc.getMessage(), response);
    }

}
