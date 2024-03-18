package ru.prodcontest.user;

import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class UserDataUtil {
    public static boolean validateUserData(User user) {
        boolean isValid = validateUserLogin(user.login)
                    && validateUserEmail(user.email)
                    && validateUserPassword(user.password)
                    && validateUserCountryCode(user.countryCode)
                    && validateUserPhone(user.phone)
                    && validateUserImage(user.image);

        return isValid;
    }

    private static boolean validateUserLogin(String login) {
        if(login.length() > 30 || login.isEmpty())
            return false;
        if(!login.matches("[a-zA-Z0-9-]+"))
            return false;

        return true;
    }

    private static boolean validateUserEmail(String email) {
        return email.length() <= 50 && !email.isEmpty();
    }

    private static boolean validateUserPassword(String password) {
        if(password.length() < 6 || password.length() > 100)
            return false;
        else if(!password.matches(".*[0-9].*"))
            return false;
        else if(!password.matches(".*[A-Z].*"))
            return false;
        else if(!password.matches(".*[a-z].*"))
            return false;

        return true;
    }

    private static boolean validateUserCountryCode(String countryCode) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/api/countries/" + countryCode)).build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean validateUserPhone(String phone) {
        return phone == null || (phone.matches("\\+[\\d]+") && phone.length() <= 20 && !phone.isEmpty());
    }

    private static boolean validateUserImage(String image) {
        return image == null || (image.length() <= 200 && !image.isEmpty());
    }

    private static SecretKeyFactory factory;
    private static byte[] salt;
    private static void init() throws NoSuchAlgorithmException {
        salt = new byte[]{-120, 21, 41, 12, 49, 28, 71, 42, 88, 19, -21, -75, 73, -1, 15, -26};
        factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    }

    public static String hashPassword(String password) {
        StringBuilder passwordHash = new StringBuilder();

        try {
            if(factory == null)
                init();

            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 64);
            byte[] hash = factory.generateSecret(spec).getEncoded();

            for(byte ch : hash)
                passwordHash.append((char) ch);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new RuntimeException(exception);
        }

        return passwordHash.toString();
    }

    public static User parseUserData(JSONObject jsonData) throws JSONException {
        String login, password, email, countryCode, phone = null, image = null;
        boolean isPublic;

        login = jsonData.getString("login");

        password = jsonData.getString("password");

        email = jsonData.getString("email");

        countryCode = jsonData.getString("countryCode");

        isPublic = jsonData.getBoolean("isPublic");

        if(jsonData.has("phone"))
            phone = jsonData.getString("phone");

        if(jsonData.has("image"))
            image = jsonData.getString("image");

        User user = new User(login, password, email, countryCode, isPublic, phone, image);
        return user;
    }

}
