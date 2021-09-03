package spring.project.forum.model.security;

import lombok.*;
import spring.project.forum.model.PostAnswer;
import spring.project.forum.model.PostQuestion;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String username;
    private String password;

    @OneToMany(mappedBy = "author")
    private List<PostQuestion> askedQuestions;

    @OneToMany(mappedBy = "author")
    private List<PostAnswer> givenAnswers;

    @ManyToMany(mappedBy = "upVotes", fetch = FetchType.EAGER)
    private List<PostQuestion> upVotedQuestions;

    @ManyToMany(mappedBy = "upVotes", fetch = FetchType.EAGER)
    private List<PostAnswer> upVotedAnswers;

    @ManyToMany(mappedBy = "downVotes", fetch = FetchType.EAGER)
    private List<PostQuestion> downVotedQuestions;

    @ManyToMany(mappedBy = "downVotes", fetch = FetchType.EAGER)
    private List<PostAnswer> downVotedAnswers;
}
