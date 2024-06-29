package ru.prodcontest.auth.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.prodcontest.user.Exceptions.UserAlreadyExistsException;
import ru.prodcontest.user.User;
import ru.prodcontest.user.UserDataUtil;
import ru.prodcontest.user.repository.UserRepository;

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
