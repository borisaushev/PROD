package ru.prodcontest.auth.register;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.prodcontest.Json.JsonUtil;
import ru.prodcontest.countries.Exceptions.NoSuchCountryException;
import ru.prodcontest.userInfo.Exceptions.UserAlreadyExistsException;

import java.util.InputMismatchException;

@ControllerAdvice("ru.prodcontest.auth.register")
@ResponseBody
public class RegisterControllerAdvice {

    @ExceptionHandler(InputMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String badRequestException(InputMismatchException exc, HttpServletResponse response) throws JSONException {
        return JsonUtil.getJsonErrorResponse(400, exc.getMessage(), response);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public String resourceNotFoundException(UserAlreadyExistsException exc, HttpServletResponse response) throws JSONException {
        return JsonUtil.getJsonErrorResponse(409, exc.getMessage(), response);
    }

    @ExceptionHandler(NoSuchCountryException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String NoSuchCountryException(NoSuchCountryException exc, HttpServletResponse response) throws JSONException {
        return JsonUtil.getJsonErrorResponse(400, exc.getMessage(), response);
    }


}