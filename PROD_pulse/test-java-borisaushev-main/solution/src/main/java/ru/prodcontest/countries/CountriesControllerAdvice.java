package ru.prodcontest.countries;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.prodcontest.Exceptions.ResultSetEmpty;
import ru.prodcontest.Json.JsonUtil;

import java.util.InputMismatchException;

@ControllerAdvice
@ResponseBody
public class CountriesControllerAdvice {

    @ExceptionHandler(InputMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String badRequestException(InputMismatchException exc, HttpServletResponse response) throws JSONException {
        return JsonUtil.getJsonErrorResponse(400, exc.getMessage(), response);
    }

    @ExceptionHandler(ResultSetEmpty.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String resourceNotFoundException(ResultSetEmpty exc, HttpServletResponse response) throws JSONException {
        return JsonUtil.getJsonErrorResponse(404, exc.getMessage(), response);
    }


}