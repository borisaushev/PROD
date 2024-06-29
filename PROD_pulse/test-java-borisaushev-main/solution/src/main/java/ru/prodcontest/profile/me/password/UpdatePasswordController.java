package ru.prodcontest.profile.me.password;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
        import ru.prodcontest.Json.JsonUtil;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;
import ru.prodcontest.profile.me.myProfile.MyProfileService;
import ru.prodcontest.user.User;

@RestController
@RequestMapping("/api/me/updatePassword")
@ResponseStatus(HttpStatus.OK)
public class UpdatePasswordController {

    @Autowired
    private MyProfileService myProfileService;
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    UpdatePasswordService updatePasswordService;

    @PostMapping
    @ResponseBody
    public String getMyProfile(@RequestHeader(name = "Authorization") String token, @RequestBody String userData) throws JSONException {

        token = jwtTokenService.parseToken(token);
        jwtTokenService.validateToken(token);
        myProfileService.validateTokenForUser(token);

        JSONObject JsonUserData = new JSONObject(userData);
        String oldPassword = JsonUserData.getString("oldPassword");
        String newPassword = JsonUserData.getString("newPassword");

        updatePasswordService.validateUserData(oldPassword, newPassword, token);

        updatePasswordService.updatePassword(newPassword, token);

        updatePasswordService.deleteAllPreviousTokens(token);

        return """
                {
                  "status": "ok"
                }
                """;

    }


}
