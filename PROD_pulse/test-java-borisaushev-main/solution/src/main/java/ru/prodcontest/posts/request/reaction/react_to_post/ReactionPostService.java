package ru.prodcontest.posts.request.reaction.react_to_post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.prodcontest.posts.post.Post;
import ru.prodcontest.posts.post.repository.PostRepository;
import ru.prodcontest.posts.request.get_post.GetPostRequestService;
import ru.prodcontest.posts.request.reaction.ReactionEntity;
import ru.prodcontest.posts.request.reaction.Reactions;
import ru.prodcontest.posts.request.reaction.reaction_repository.ReactionRepository;
import ru.prodcontest.userInfo.repository.UserRepository;

import java.util.UUID;

@Service
public class ReactionPostService {

    @Autowired
    ReactionRepository reactionRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GetPostRequestService getPostRequestService;

    public Post reactToPost(UUID postId, int requestUserId, Reactions reactionType) {

        String login = userRepository.getLoginById(requestUserId);
        Post post = getPostRequestService.getPostAndCheckAccess(requestUserId, postId);

        updateReaction(post, login, reactionType);

        updatePost(post);

        return post;
    }

    private void updateReaction(Post post, String login, Reactions reactionType) {
        reactionRepository.deleteLastReaction(post.getId(), login);

        ReactionEntity reaction = new ReactionEntity();
        reaction.setReactionType(reactionType);
        reaction.setPost(post);
        reaction.setAuthor(login);

        reactionRepository.save(reaction);
    }

    private void updatePost(Post post) {
        long dislikesCount = reactionRepository.countReactionWithType(post.getId(), Reactions.Dislike);
        long likesCount = reactionRepository.countReactionWithType(post.getId(), Reactions.Like);

        post.setDislikesCount(dislikesCount);
        post.setLikesCount(likesCount);

        postRepository.save(post);

    }


}
