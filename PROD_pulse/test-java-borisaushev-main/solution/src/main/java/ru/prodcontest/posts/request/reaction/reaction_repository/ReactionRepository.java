package ru.prodcontest.posts.request.reaction.reaction_repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.prodcontest.posts.request.reaction.ReactionEntity;
import ru.prodcontest.posts.request.reaction.Reactions;

import java.util.UUID;

@Repository
public interface ReactionRepository extends JpaRepository<ReactionEntity, UUID> {

    @Transactional
    @Modifying
    @Query("Delete ReactionEntity r Where r.author = :author And r.post.id = :postId")
    void deleteLastReaction(@Param("postId") UUID postId, @Param("author") String author);

    @Transactional
    @Query("select count(*) from ReactionEntity r where r.post.id = :postId " +
            "and r.reactionType = :reactionType")
    long countReactionWithType(@Param("postId") UUID postId, @Param("reactionType") Reactions reactionType);

}
