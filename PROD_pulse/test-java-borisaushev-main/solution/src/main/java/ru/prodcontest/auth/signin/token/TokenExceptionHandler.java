package ru.prodcontest.auth.signin.token;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.prodcontest.Json.JsonUtil;
import ru.prodcontest.auth.signin.token.Exceptions.TokenDoesntExistException;
import ru.prodcontest.auth.signin.token.Exceptions.TokenExpiredException;

@ControllerAdvice("ru.prodcontest")
public class TokenExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenExpiredException.class)
    @ResponseBody
    public String notFoundExceptionHandling(TokenExpiredException exc, HttpServletResponse response) throws JSONException {
        return JsonUtil.getJsonErrorResponse(401, exc.getMessage(), response);
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenDoesntExistException.class)
    @ResponseBody
    public String TokenDoesntExistExceptionHandling(TokenDoesntExistException exc, HttpServletResponse response) throws JSONException {
        return JsonUtil.getJsonErrorResponse(401, exc.getMessage(), response);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(MalformedJwtException.class)
    @ResponseBody
    public String TokenError(MalformedJwtException exc, HttpServletResponse response) throws JSONException {
        return JsonUtil.getJsonErrorResponse(401, exc.getMessage(), response);
    }

}