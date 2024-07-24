package ru.prodcontest.posts.request.create_post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.prodcontest.posts.post.Post;

import java.util.UUID;

@Repository
public interface CreatePostRepository extends JpaRepository<Post, UUID> {

}
