package ru.prodcontest.posts.request.reaction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.prodcontest.posts.post.Post;

@Getter
@Setter
@Entity
@Table(name = "last_reactions")
public class ReactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name="post_id")
    private Post post;

    @Column(name = "author")
    private String author;

    @Enumerated(value = EnumType.STRING)
    private Reactions reactionType;

}
