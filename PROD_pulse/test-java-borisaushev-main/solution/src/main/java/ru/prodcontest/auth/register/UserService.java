package ru.prodcontest.auth.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.prodcontest.userInfo.Exceptions.UserAlreadyExistsException;
import ru.prodcontest.userInfo.User;
import ru.prodcontest.userInfo.UserDataUtil;
import ru.prodcontest.userInfo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void checkIfUserAlreadyExists(User user) {
        if(userRepository.userExists(user))
            throw new UserAlreadyExistsException("user already exists");
    }

    public void saveUser(User user) {
        String hashedPassword = UserDataUtil.hashPassword(user.password());
        userRepository.saveUser(user, hashedPassword);
    }

}
