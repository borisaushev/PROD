package ru.prodcontest.Json;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import ru.prodcontest.user.User;

import java.net.http.HttpResponse;

public class JsonUtil {
    public static String getJsonErrorResponse(int status, String reason, HttpServletResponse httpResponse) throws JSONException {
        httpResponse.setStatus(status);
        JSONObject JsonResponseObject = new JSONObject();
        JsonResponseObject.put("reason", reason);
        return JsonResponseObject.toString();
    }

    public static void fillJsonObjectWithUserData(JSONObject jsonObject, User user) {
        try {

            jsonObject.put("login", user.login);
            jsonObject.put("email", user.email);
            jsonObject.put("countryCode", user.countryCode);
            jsonObject.put("isPublic", user.isPublic);

            if (user.phone != null)
                jsonObject.put("phone", user.phone);

            if (user.image != null)
                jsonObject.put("image", user.image);

        } catch (JSONException exc) {
            throw new RuntimeException(exc);
        }
    }

}
