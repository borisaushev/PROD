package ru.prodcontest.posts.request.get_ones_feed;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.prodcontest.posts.post.Post;

import java.util.List;
import java.util.UUID;

@Repository
public interface GetOnesFeedRepository extends JpaRepository<Post, UUID> {

    @Query(value = "SELECT * FROM Posts WHERE author = :login ORDER BY created_at DESC offset :offset limit :limit", nativeQuery = true)
    List<Post> getOnesFeed(@Param("login") String login, @Param("limit") int limit, @Param("offset") int offset);

}
