package ru.prodcontest.posts.request.create_post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.prodcontest.posts.post.Post;
import ru.prodcontest.posts.post.repository.PostRepository;
import ru.prodcontest.posts.post.request_object.CreatePostRequestObject;
import ru.prodcontest.userInfo.repository.UserRepository;

import java.sql.Date;
import java.time.Instant;

@Service
public class CreatePostService {
    @Autowired
    PostRepository repository;

    @Autowired
    private UserRepository userRepository;

    public Post savePost(CreatePostRequestObject postInfo, int userId) {
        String login = userRepository.getLoginById(userId);

        Post post = new Post();
        post.setCreatedAt(Date.from(Instant.now()));
        post.setContent(postInfo.getContent());
        post.setTags(postInfo.getTags());
        post.setAuthor(login);
        post.setDislikesCount(0);
        post.setLikesCount(0);

        Post savedPost = repository.save(post);

        return savedPost;

    }
}
