package ru.prodcontest.profile.me.password;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;
import ru.prodcontest.profile.me.myProfile.MyProfileService;
import ru.prodcontest.user.User;
import ru.prodcontest.user.UserDataUtil;
import ru.prodcontest.user.repository.UserRepository;

import java.util.InputMismatchException;

@Service
public class UpdatePasswordService {

    @Autowired
    private MyProfileService myProfileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    public void validateUserData(String oldPassword, String newPassword, String token) {
        validateOldPassword(oldPassword, token);
        validateNewPassword(newPassword);
    }

    private void validateOldPassword(String oldPassword, String token) {
        User user = myProfileService.getUserByToken(token);
        String hashedOldPassword = UserDataUtil.hashPassword(oldPassword);
        if(user.password().equals(hashedOldPassword) == Boolean.FALSE)
            throw new InputMismatchException("неверный пароль");
    }

    private void validateNewPassword(String newPassword) {
        if(UserDataUtil.validateUserPassword(newPassword) == Boolean.FALSE)
            throw new SecurityException("ненадежный пароль");
    }

    public void updatePassword(String newPassword, String token) {
        int userId = jwtTokenService.getIdByToken(token);
        String hashedNewPassword = UserDataUtil.hashPassword(newPassword );
        userRepository.updateUserProperty(userId, "password", hashedNewPassword);
    }

    public void deleteAllPreviousTokens(String token) {
        int userId = jwtTokenService.getIdByToken(token);
        userRepository.deleteTokensById(userId);
    }

}
