package ru.prodcontest.Json;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import ru.prodcontest.userInfo.User;

public class JsonUtil {
    public static String getJsonErrorResponse(int status, String reason, HttpServletResponse httpResponse) throws JSONException {
        httpResponse.setStatus(status);
        JSONObject JsonResponseObject = new JSONObject();
        JsonResponseObject.put("reason", reason);
        return JsonResponseObject.toString();
    }

    public static JSONObject getUserJson(User user) throws JSONException {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("login", user.login());
            jsonObject.put("email", user.email());
            jsonObject.put("countryCode", user.countryCode());
            jsonObject.put("isPublic", user.isPublic());
            jsonObject.put("phone", user.phone());
            jsonObject.put("image", user.image());

            JSONObject profileJson = new JSONObject();
            profileJson.put("profile", jsonObject);

            return profileJson;
    }

}
