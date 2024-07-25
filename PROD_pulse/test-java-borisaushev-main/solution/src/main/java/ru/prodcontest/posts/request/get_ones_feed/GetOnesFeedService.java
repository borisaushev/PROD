package ru.prodcontest.posts.request.get_ones_feed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.prodcontest.auth.signin.Exceptions.UserDoesntExistsException;
import ru.prodcontest.posts.post.Post;
import ru.prodcontest.posts.post.exception.PostIsPrivateException;
import ru.prodcontest.posts.post.repository.PostRepository;
import ru.prodcontest.userInfo.User;
import ru.prodcontest.userInfo.repository.UserRepository;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class GetOnesFeedService {

    @Autowired
    PostRepository repository;

    @Autowired
    UserRepository userRepository;


    public List<Post> getOnesFeed(String login, int requestUserId, int limit, int offset) {

        if(userRepository.userExistsWithProperty("login", login) == false)
            throw new UserDoesntExistsException("no user with such login exists");

        int userId = userRepository.getIdByLogin(login);
        User user = userRepository.getUserById(userId);

        boolean userIsPublic = user.isPublic();
        boolean userIsFriend = userRepository.getUserFriends(userId).contains(requestUserId);

        if(userIsPublic == false && userIsFriend == false && userId != requestUserId)
            throw new PostIsPrivateException("cannot access ones feed");

        List<Post> onesFeed = repository.getOnesFeed(login, limit, offset);

        return onesFeed;

    }
}
