package spring.project.forum.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import spring.project.forum.model.security.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Lob
    private String content;

    //    @CreationTimestamp
    private LocalDate createdAt;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "username")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    private User author;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "username")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "user_upvotedanswer",
            joinColumns = {@JoinColumn(name = "answer_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> upVotes;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "username")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "user_downvotedanswer",
            joinColumns = {@JoinColumn(name = "answer_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> downVotes;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    private Question targetQuestion;

    @Builder.Default
    private Boolean isBestAnswer = false;
}
