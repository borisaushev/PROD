package ru.prodcontest.profile.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;
import ru.prodcontest.userInfo.User;
import ru.prodcontest.userInfo.repository.UserRepository;

import java.util.InputMismatchException;
import java.util.Objects;

@Service
public class ProfileByLoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtTokenService jwtTokenService;

    public void validateLogin(String login) {
        if(userRepository.userExistsWithProperty("login", login) == Boolean.FALSE)
            throw new InputMismatchException("no user with such login");
    }

    public void checkIfUserProfileIsAvailable(String login, String token) {
        int requestAuthorId = jwtTokenService.getIdByToken(token);
        int requestedProfileId = userRepository.getIdByLogin(login);

        User requestAuthorProfile = userRepository.getUserById(requestAuthorId);
        User requestedProfile = userRepository.getUserByLogin(login);

        if(requestedProfile.isPublic() || profilesAreSame(requestAuthorProfile, requestAuthorProfile))
            return;

        if(userRepository.getUserFriends(requestedProfileId).contains(requestAuthorId))
            return;

        throw new InputMismatchException("profile not available");

    }

    private boolean profilesAreSame(User u1, User u2) {
        return Objects.equals(u1.login(), u2.login());
    }

    public User getRequestedUserProfile(String login) {
        User requestedProfile = userRepository.getUserByLogin(login);
        return requestedProfile;
    }

}
