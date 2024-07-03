package ru.prodcontest.posts.create;


import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.prodcontest.Json.JsonUtil;

@ControllerAdvice("ru.prodcontest.posts.create")
@ResponseBody
public class RequestControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String someException(Exception exc, HttpServletResponse response) throws JSONException {
        return JsonUtil.getJsonErrorResponse(200, exc.getMessage(), response);
    }

}
