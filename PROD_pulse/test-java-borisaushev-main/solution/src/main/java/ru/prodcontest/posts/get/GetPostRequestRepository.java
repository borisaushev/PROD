package ru.prodcontest.posts.get;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.prodcontest.posts.post.Post;

import java.util.UUID;

@Repository
public interface GetPostRequestRepository extends JpaRepository<Post, UUID> {


}
