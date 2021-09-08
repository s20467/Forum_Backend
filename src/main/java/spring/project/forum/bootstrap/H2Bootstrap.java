package spring.project.forum.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import spring.project.forum.model.PostAnswer;
import spring.project.forum.model.PostQuestion;
import spring.project.forum.model.security.User;
import spring.project.forum.repository.PostAnswerRepository;
import spring.project.forum.repository.PostQuestionRepository;
import spring.project.forum.repository.security.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
@Profile("h2")
public class H2Bootstrap implements CommandLineRunner {

    private final PostQuestionRepository questionRepository;
    private final PostAnswerRepository answerRepository;
    private final UserRepository userRepository;

    public H2Bootstrap(PostQuestionRepository questionRepository, PostAnswerRepository answerRepository, UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {


        PostQuestion q1 = PostQuestion.builder()
                .title("example question title 1")
                .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                .build();

        PostQuestion q2 = PostQuestion.builder()
                .title("example question title 2")
                .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                .build();

        PostQuestion q3 = PostQuestion.builder()
                .title("example question title 3")
                .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                .build();

        PostQuestion q4 = PostQuestion.builder()
                .title("example question title 4")
                .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                .build();

        PostQuestion q5 = PostQuestion.builder()
                .title("example question title 5")
                .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                .build();

        PostAnswer a1 = PostAnswer.builder()
                .content("Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem")
                .targetQuestion(q1)
                .build();

        PostAnswer a2 = PostAnswer.builder()
                .content("Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem")
                .targetQuestion(q1)
                .isBestAnswer(true)
                .build();

        PostAnswer a3 = PostAnswer.builder()
                .content("Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem")
                .targetQuestion(q1)
                .build();

        q1.setAnswers(List.of(a1, a2));
        q1.setBestAnswer(a2);
        q2.setAnswers(List.of(a3));

        a1.setTargetQuestion(q1);
        a2.setTargetQuestion(q1);
        a3.setTargetQuestion(q2);



        User u1 = User.builder()
                .username("user1")
                .password("passs")
                .askedQuestion(q1)
                .askedQuestion(q2)
                .askedQuestion(q3)
                .askedQuestion(q4)
                .askedQuestion(q5)
                .givenAnswer(a1)
                .givenAnswer(a2)
                .givenAnswer(a3)
                .upVotedAnswer(a3)
                .build();

        q1.setAuthor(u1);
        q2.setAuthor(u1);

        a1.setAuthor(u1);
        a2.setAuthor(u1);
        a3.setAuthor(u1);

        a3.setUpVotes(List.of(u1));

        userRepository.save(u1);

        //questionRepository.saveAll(Set.of(q1, q2));
    }
}
