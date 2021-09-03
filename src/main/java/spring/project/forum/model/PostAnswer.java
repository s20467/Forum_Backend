package spring.project.forum.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import spring.project.forum.model.security.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "answer")
public class PostAnswer{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Lob
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    private User author;

    @ManyToMany
    @JoinTable(
            name = "user_upvotedanswer",
            joinColumns = {@JoinColumn(name = "answer_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> upVotes;

    @ManyToMany
    @JoinTable(
            name = "user_downvotedanswer",
            joinColumns = {@JoinColumn(name = "answer_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> downVotes;

    @ManyToOne(fetch = FetchType.EAGER)
    private PostQuestion targetQuestion;

    private Boolean isBestAnswer;
}
