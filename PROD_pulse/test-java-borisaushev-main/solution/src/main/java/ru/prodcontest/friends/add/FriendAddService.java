package ru.prodcontest.friends.add;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.prodcontest.auth.signin.token.Jwt.JwtTokenService;
import ru.prodcontest.userInfo.User;
import ru.prodcontest.userInfo.repository.UserRepository;

import java.util.InputMismatchException;
import java.util.Objects;

@Service
public class FriendAddService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    FriendAddRepository friendAddRepository;

    @Autowired
    JwtTokenService jwtTokenService;

    public void validateLogin(String login) {
        if(userRepository.userExistsWithProperty("login", login) == Boolean.FALSE)
            throw new InputMismatchException("no user with such login");
    }

    public void addFriend(String login, String token) {
        int requestAuthorId = jwtTokenService.getIdByToken(token);
        int friendToAddId = userRepository.getIdByLogin(login);

        User requestAuthorProfile = userRepository.getUserById(requestAuthorId);
        User requestedProfile = userRepository.getUserByLogin(login);

        if(profilesAreSame(requestedProfile, requestAuthorProfile) || userRepository.getUserFriends(requestAuthorId).contains(friendToAddId))
            return;

        if(userRepository.getUserFriends(requestAuthorId).contains(friendToAddId))
            return;

        friendAddRepository.addFriend(requestAuthorId, friendToAddId);

    }

    private boolean profilesAreSame(User u1, User u2) {
        return Objects.equals(u1.login(), u2.login());
    }

}
