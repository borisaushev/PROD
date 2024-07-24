package ru.prodcontest.posts.request.get_my_feed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.prodcontest.posts.post.Post;

import java.util.List;
import java.util.UUID;

@Repository
public interface GetMyFeedRepository extends JpaRepository<Post, UUID> {

    @Query(value = "SELECT * FROM Posts WHERE author = :login ORDER BY created_at DESC offset :offset limit :limit", nativeQuery = true)
    List<Post> getMyFeed(@Param("login") String login, @Param("limit") int limit, @Param("offset") int offset);

}