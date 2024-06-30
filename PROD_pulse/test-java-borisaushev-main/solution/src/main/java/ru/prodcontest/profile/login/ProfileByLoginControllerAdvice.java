package ru.prodcontest.profile.login;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.prodcontest.Json.JsonUtil;

import java.util.InputMismatchException;

@ControllerAdvice("ru.prodcontest.profile.login")
@ResponseBody
public class ProfileByLoginControllerAdvice {

    @ExceptionHandler(InputMismatchException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public String badRequestException(InputMismatchException exc, HttpServletResponse response) throws JSONException {
        return JsonUtil.getJsonErrorResponse(403, exc.getMessage(), response);
    }
}