package ru.prodcontest.posts.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.prodcontest.posts.post.Post;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query(value = "SELECT * FROM Posts WHERE author = :login ORDER BY created_at DESC offset :offset limit :limit", nativeQuery = true)
    List<Post> getOnesFeed(@Param("login") String login, @Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT * FROM Posts WHERE author = :login ORDER BY created_at DESC offset :offset limit :limit", nativeQuery = true)
    List<Post> getMyFeed(@Param("login") String login, @Param("limit") int limit, @Param("offset") int offset);


}
