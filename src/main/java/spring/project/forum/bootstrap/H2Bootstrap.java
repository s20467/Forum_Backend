package spring.project.forum.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import spring.project.forum.model.Answer;
import spring.project.forum.model.Question;
import spring.project.forum.model.security.Authority;
import spring.project.forum.model.security.Role;
import spring.project.forum.model.security.User;
import spring.project.forum.repository.AnswerRepository;
import spring.project.forum.repository.QuestionRepository;
import spring.project.forum.repository.security.AuthorityRepository;
import spring.project.forum.repository.security.RoleRepository;
import spring.project.forum.repository.security.UserRepository;

import java.util.List;
import java.util.Set;

@Component
@Profile("h2")
public class H2Bootstrap implements CommandLineRunner {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public H2Bootstrap(QuestionRepository questionRepository, AnswerRepository answerRepository, UserRepository userRepository, RoleRepository roleRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

//        Authority questionCreateAuthority = Authority.builder().permission("question.create").build();
//        Authority answerCreateAuthority = Authority.builder().permission("answer.create").build();

//        Authority userQuestionUpdateAuthority = Authority.builder().permission("user.question.update").build();
//        Authority userQuestionDeleteAuthority = Authority.builder().permission("user.question.delete").build();
//        Authority userQuestionCloseAuthority = Authority.builder().permission("user.question.close").build();
//        Authority userQuestionSetBestAnswerAuthority = Authority.builder().permission("user.question.set-best-answer").build();

        Authority adminQuestionUpdateAuthority = Authority.builder().permission("admin.question.update").build();
        Authority adminQuestionDeleteAuthority = Authority.builder().permission("admin.question.delete").build();
        Authority adminQuestionCloseAuthority = Authority.builder().permission("admin.question.close").build();
        Authority adminQuestionSetBestAnswerAuthority = Authority.builder().permission("admin.question.set-unset-best-answer").build();

//        Authority userAnswerUpdateAuthority = Authority.builder().permission("user.answer.update").build();
//        Authority userAnswerDeleteAuthority = Authority.builder().permission("user.answer.delete").build();

        Authority adminAnswerUpdateAuthority = Authority.builder().permission("admin.answer.update").build();
        Authority adminAnswerDeleteAuthority = Authority.builder().permission("admin.answer.delete").build();

        Role userRole = Role.builder()
                .name("ROLE_USER")
                .authorities(List.of(
//                        questionCreateAuthority,
//                        userQuestionUpdateAuthority,
//                        userQuestionDeleteAuthority,
//                        userQuestionCloseAuthority,
//                        userQuestionSetBestAnswerAuthority,
//                        answerCreateAuthority,
//                        userAnswerUpdateAuthority,
//                        userAnswerDeleteAuthority
                ))
                .build();
        Role adminRole = Role.builder()
                .name("ROLE_ADMIN")
                .authorities(List.of(
                        adminQuestionCloseAuthority,
                        adminQuestionUpdateAuthority,
                        adminQuestionDeleteAuthority,
                        adminAnswerUpdateAuthority,
                        adminAnswerDeleteAuthority,
                        adminQuestionSetBestAnswerAuthority
                )).build();



        Question q1 = Question.builder()
                .title("example question title 1")
                .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                .build();

        Question q2 = Question.builder()
                .title("example question title 2")
                .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                .build();

        Question q3 = Question.builder()
                .title("example question title 3")
                .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                .build();

        Question q4 = Question.builder()
                .title("example question title 4")
                .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                .build();

        Question q5 = Question.builder()
                .title("example question title 5")
                .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                .build();

        Answer a1 = Answer.builder()
                .content("Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem")
                .targetQuestion(q1)
                .build();

        Answer a2 = Answer.builder()
                .content("Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem")
                .targetQuestion(q1)
                .isBestAnswer(true)
                .build();

        Answer a3 = Answer.builder()
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
                .username("user")
                .password(passwordEncoder.encode("pass"))
                .role(userRole)
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

        User u2 = User.builder()
                .username("user2")
                .password(passwordEncoder.encode("pass"))
                .role(userRole)
                .build();

        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("pass"))
                .role(adminRole)
                .role(userRole)
                .build();

        q1.setAuthor(u1);
        q2.setAuthor(u1);

        a1.setAuthor(u1);
        a2.setAuthor(u1);
        a3.setAuthor(u1);

        a3.setUpVotes(List.of(u1));

        userRepository.saveAll(Set.of(u1, u2, admin));

        //questionRepository.saveAll(Set.of(q1, q2));
    }
}
