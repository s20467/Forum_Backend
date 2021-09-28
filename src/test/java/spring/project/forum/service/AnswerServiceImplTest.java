package spring.project.forum.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import spring.project.forum.api.v1.dto.AnswerDto;
import spring.project.forum.exception.IncorrectPageableException;
import spring.project.forum.exception.ResourceNotFoundException;
import spring.project.forum.model.Answer;
import spring.project.forum.model.Question;
import spring.project.forum.model.security.User;
import spring.project.forum.repository.AnswerRepository;
import spring.project.forum.repository.QuestionRepository;
import spring.project.forum.repository.security.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AnswerServiceImplTest {

    @Mock
    QuestionRepository questionRepository;
    @Mock
    AnswerRepository answerRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    PropertyReferenceException propertyReferenceException;

    @InjectMocks
    AnswerServiceImpl answerService;

    Answer answer1;
    Answer answer2;
    AnswerDto answerDto1;
    List<Answer> listOfAnswers;
    Page<Answer> pageOfAnswers;

    @BeforeEach
    void setUp() {
        answer1 = Answer.builder().id(1).content("content1").build();
        answer2 = Answer.builder().id(2).content("content2").build();
        answerDto1 = AnswerDto.builder().content("contentDto1").build();
        listOfAnswers = List.of(answer1, answer2);
        pageOfAnswers = new PageImpl<>(listOfAnswers);
    }

    @Nested
    @DisplayName("get all answers")
    class getAllAnswers {

        @Nested
        @DisplayName(" - unpaged")
        class getAllAnswersUnpaged {

            @Test
            @DisplayName(" - correct")
            void getAllAnswersCorrect() {
                given(answerRepository.findAll()).willReturn(listOfAnswers);

                List<Answer> foundAnswers = answerService.getAll();

                assertEquals(listOfAnswers, foundAnswers);
            }
        }

        @Nested
        @DisplayName(" - paged")
        class getAllAnswersPaged {

            @Test
            @DisplayName(" - incorrect pagination arguments")
            void getAllAnswersPagedIncorrectPaginationArguments() {
                given(answerRepository.findAll(any(Pageable.class))).willThrow(propertyReferenceException);

                assertThrows(IncorrectPageableException.class, () -> answerService.getAll(1, 1, "sort"));
            }

            @Test
            @DisplayName(" - correct")
            void getAllAnswersPagedCorrect() {
                given(answerRepository.findAll(any(Pageable.class))).willReturn(pageOfAnswers);

                Page<Answer> foundAnswers = answerService.getAll(1, 1, "sort");

                assertEquals(pageOfAnswers, foundAnswers);
            }
        }
    }

    @Nested
    @DisplayName("get answer by id")
    class getAnswerById {

        @Test
        @DisplayName(" - not existing answer id")
        void getAnswerByIdWrongId() {
            given(answerRepository.findById(anyInt())).willReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> answerService.getById(1));
        }

        @Test
        @DisplayName(" - correct")
        void getAnswerByIdCorrect() {
            given(answerRepository.findById(anyInt())).willReturn(Optional.of(answer1));

            Answer foundAnswer = answerService.getById(answer1.getId());

            assertEquals(answer1, foundAnswer);
        }
    }

    @Nested
    @DisplayName("delete answer by id")
    class deleteAnswerById {

        @Test
        @DisplayName(" - not existing answer id")
        void deleteAnswerByIdWrongId() {
            given(answerRepository.findById(anyInt())).willReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> answerService.deleteById(1));
        }

        @Test
        @DisplayName(" - correct")
        void deleteAnswerByIdCorrect() {
            given(answerRepository.findById(anyInt())).willReturn(Optional.of(answer1));

            answerService.deleteById(answer1.getId());
        }
    }

    @Disabled
    @Nested
    @DisplayName("create answer")
    class createAnswer {
        //todo implement createAnswer method test when createAnswer gets more business logic
    }

    @Disabled
    @Nested
    @DisplayName("update answer content")
    class updateAnswerContent {

        @Test
        @DisplayName(" - not existing answer id")
        void updateAnswerWrongId() {
            given(answerRepository.findById(anyInt())).willReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> answerService.updateAnswerContent(1, new AnswerDto()));
        }

        @Test
        @DisplayName(" - correct")
        void updateAnswerCorrect() {
            given(answerRepository.findById(anyInt())).willReturn(Optional.of(answer1));
            given(answerRepository.save(any(Answer.class))).willAnswer(invocation -> invocation.getArgument(0));

            Answer returnedAnswer = answerService.updateAnswerContent(answer1.getId(), answerDto1);

            assertEquals(answerDto1.getContent(), returnedAnswer.getContent());
        }
    }

    @Nested
    @DisplayName("get answers by question")
    class getAnswersByQuestion {

        @Nested
        @DisplayName(" - unpaged")
        class getAnswersByQuestionUnpaged {

            @Test
            @DisplayName(" - not existing question id")
            void getAnswersByQuestionWrongQuestionId() {
                given(questionRepository.findById(anyInt())).willReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, () -> answerService.getByQuestion(1));
            }

            @Test
            @DisplayName(" - correct")
            void getAnswersByQuestionCorrect() {
                given(questionRepository.findById(anyInt())).willReturn(Optional.of(new Question()));
                given(answerRepository.findAllByTargetQuestion(any(Pageable.class), any(Question.class))).willReturn(pageOfAnswers);

                Page<Answer> resultAnswerPage = answerService.getByQuestion(1, 1, 1, "sort");

                assertEquals(pageOfAnswers, resultAnswerPage);
            }
        }

        @Nested
        @DisplayName(" - paged")
        class getAnswersByQuestionPaged {

            @Test
            @DisplayName(" - not existing question id")
            void getAnswersByQuestionPagedWrongQuestionId() {
                given(questionRepository.findById(anyInt())).willReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, () -> answerService.getByQuestion(1, 1, 1, "sort"));
            }

            @Test
            @DisplayName(" - incorrect pagination arguments")
            void getAnswersByQuestionPagedIncorrectPaginationArguments() {
                given(questionRepository.findById(anyInt())).willReturn(Optional.of(new Question()));
                given(answerRepository.findAllByTargetQuestion(any(), any())).willThrow(propertyReferenceException);

                assertThrows(IncorrectPageableException.class, () -> answerService.getByQuestion(1, 1, 1, "sort"));
            }

            @Test
            @DisplayName(" - correct")
            void getAnswersByQuestionPagedCorrect() {
                given(questionRepository.findById(anyInt())).willReturn(Optional.of(new Question()));
                given(answerRepository.findAllByTargetQuestion(any(Pageable.class), any(Question.class))).willReturn(pageOfAnswers);

                Page<Answer> resultAnswerPage = answerService.getByQuestion(1, 1, 1, "sort");

                assertEquals(pageOfAnswers, resultAnswerPage);
            }
        }
    }


    @Nested
    @DisplayName("get answers by author")
    class getAnswersByAuthor {

        @Nested
        @DisplayName(" - unpaged")
        class getAnswersByAuthorUnpaged {

            @Test
            @DisplayName(" - not existing author username")
            void getAnswersByAuthorWrongAuthorUsername() {
                given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, () -> answerService.getByAuthor("username"));
            }

            @Test
            @DisplayName(" - correct")
            void getAnswersByAuthorCorrect() {
                given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
                given(answerRepository.findAllByAuthor(any(User.class))).willReturn(listOfAnswers);

                List<Answer> resultAnswers = answerService.getByAuthor("username");

                assertEquals(listOfAnswers, resultAnswers);
            }
        }

        @Nested
        @DisplayName(" - paged")
        class getAnswersByAuthorPaged {

            @Test
            @DisplayName(" - not existing author username")
            void getAnswersByAuthorPagedWrongAuthorUsername() {
                given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, () -> answerService.getByAuthor("username", 1, 1, "sort"));
            }

            @Test
            @DisplayName(" - incorrect pagination arguments")
            void getAnswersByAuthorPagedIncorrectPaginationArguments() {
                given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
                given(answerRepository.findAllByAuthor(any(), any())).willThrow(propertyReferenceException);

                assertThrows(IncorrectPageableException.class, () -> answerService.getByAuthor("username", 1, 1, "sort"));
            }

            @Test
            @DisplayName(" - correct")
            void getAnswersByAuthorPagedCorrect() {
                given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
                given(answerRepository.findAllByAuthor(any(Pageable.class), any(User.class))).willReturn(pageOfAnswers);

                Page<Answer> resultAnswerPage = answerService.getByAuthor("username", 1, 1, "sortBy");

                assertEquals(pageOfAnswers, resultAnswerPage);
            }
        }
    }
}