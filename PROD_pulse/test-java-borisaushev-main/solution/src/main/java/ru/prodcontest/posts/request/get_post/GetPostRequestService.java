package ru.prodcontest.posts.request.get_post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.prodcontest.posts.post.Post;
import ru.prodcontest.posts.post.exception.PostIsPrivateException;
import ru.prodcontest.posts.post.exception.PostNotFoundException;
import ru.prodcontest.userInfo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GetPostRequestService {
    @Autowired
    GetPostRequestRepository getPostRequestRepository;

    @Autowired
    UserRepository userRepository;

    public Post getPost(int userId, UUID postId) {

        Optional<Post> postResult = getPostRequestRepository.findById(postId);

        if(postResult.isEmpty())
            throw new PostNotFoundException("no such post found");

        Post post = postResult.get();

        String authorLogin = post.getAuthor();
        int authorId = userRepository.getIdByLogin(authorLogin);

        boolean postIsPublic = userRepository.getUserById(authorId).isPublic();

        if(!postIsPublic && authorId != userId) {
            List<Integer> friends = userRepository.getUserFriends(authorId);
            if(!friends.contains(userId))
                throw new PostIsPrivateException("cannot access this post");
        }

        return post;

    }
}
