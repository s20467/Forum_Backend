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
@Table(name = "question")
public class PostQuestion{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
    @Lob
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime closedAt;

    @ManyToOne
    private User author;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_upvotedquestion",
            joinColumns = {@JoinColumn(name = "question_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> upVotes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_downvotedquestion",
            joinColumns = {@JoinColumn(name = "question_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> downVotes;

    @OneToOne
    private PostAnswer bestAnswer;

    @OneToMany(mappedBy = "targetQuestion", fetch = FetchType.EAGER)
    private List<PostAnswer> answers;
}
