package ru.prodcontest.profile.me.password;


import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.prodcontest.Json.JsonUtil;

import java.util.InputMismatchException;

@ControllerAdvice("ru.prodcontest.profile.me.password")
@ResponseBody
public class UpdatePasswordExceptionAdvice {


    @ExceptionHandler(InputMismatchException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public String InputMismatchException(InputMismatchException exc, HttpServletResponse response) throws JSONException {
        return JsonUtil.getJsonErrorResponse(403, exc.getMessage(), response);
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String SecurityException(SecurityException exc, HttpServletResponse response) throws JSONException {
        return JsonUtil.getJsonErrorResponse(400, exc.getMessage(), response);
    }
}
