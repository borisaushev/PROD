package ru.prodcontest.auth.signin;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.prodcontest.Json.JsonUtil;
import ru.prodcontest.auth.signin.Exceptions.UserDoesntExistsException;

@ControllerAdvice("ru.prodcontest.auth.signin")
@ResponseBody
public class SignInControllerAdvice {

    @ExceptionHandler(UserDoesntExistsException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public String badRequestException(UserDoesntExistsException exc, HttpServletResponse response) throws JSONException {
        return JsonUtil.getJsonErrorResponse(401, exc.getMessage(), response);
    }


}