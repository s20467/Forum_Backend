package spring.project.forum.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import spring.project.forum.exception.IncorrectPageableException;
import spring.project.forum.exception.QuestionAlreadyClosedException;
import spring.project.forum.exception.ResourceNotFoundException;
import spring.project.forum.model.Question;
import spring.project.forum.model.security.User;
import spring.project.forum.repository.AnswerRepository;
import spring.project.forum.repository.QuestionRepository;
import spring.project.forum.repository.security.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    QuestionRepository questionRepository;
    @Mock
    AnswerRepository answerRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    PropertyReferenceException propertyReferenceException;

    @InjectMocks
    QuestionServiceImpl questionService;

    @Nested
    @DisplayName("get all questions")
    class getAllQuestions{

        @Nested
        @DisplayName(" - unpaged")
        class getAllQuestionsUnpaged{

            @Test
            @DisplayName(" - correct")
            void getAllQuestionsCorrect(){
                Question question1 = Question.builder().id(1).title("title1").content("content1").build();
                Question question2 = Question.builder().id(2).title("title2").content("content2").build();
                List<Question> questions = List.of(question1, question2);
                given(questionRepository.findAll()).willReturn(questions);

                List<Question> foundQuestions = questionService.getAll();

                assertEquals(questions, foundQuestions);
            }
        }

        @Nested
        @DisplayName(" - paged")
        class getAllQuestionsPaged{

            @Test
            @DisplayName(" - incorrect pagination arguments")
            void getAllQuestionsPagedIncorrectPaginationArguments(){
                given(questionRepository.findAll(any(Pageable.class))).willThrow(propertyReferenceException);

                assertThrows(IncorrectPageableException.class, () -> questionService.getAll(1, 1, "sort"));
            }

            @Test
            @DisplayName(" - correct")
            void getAllQuestionsPagedCorrect(){
                Question question1 = Question.builder().id(1).title("title1").content("content1").build();
                Question question2 = Question.builder().id(2).title("title2").content("content2").build();
                Page<Question> questions = new PageImpl<>(List.of(question1, question2));
                given(questionRepository.findAll(any(Pageable.class))).willReturn(questions);

                Page<Question> foundQuestions = questionService.getAll(1, 1, "sort");

                assertEquals(questions, foundQuestions);
            }
        }
    }

    @Nested
    @DisplayName("get question by id")
    class getQuestionById{

        @Test
        @DisplayName(" - not existing question id")
        void getQuestionByIdWrongId(){
            given(questionRepository.findById(anyInt())).willReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> questionService.getById(1));
        }

        @Test
        @DisplayName(" - correct")
        void getQuestionByIdCorrect(){
            Question question = Question.builder().id(1).title("title1").content("content1").build();
            given(questionRepository.findById(anyInt())).willReturn(Optional.of(question));

            Question foundQuestion = questionService.getById(question.getId());

            assertEquals(question, foundQuestion);
        }
    }

    @Nested
    @DisplayName("delete question by id")
    class deleteQuestionById{

        @Test
        @DisplayName(" - not existing question id")
        void deleteQuestionByIdWrongId(){
            given(questionRepository.findById(anyInt())).willReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> questionService.deleteById(1));
        }

        @Test
        @DisplayName(" - correct")
        void deleteQuestionByIdCorrect(){
            Question question = Question.builder().id(1).title("title1").content("content1").build();
            given(questionRepository.findById(anyInt())).willReturn(Optional.of(question));

            Question deletedQuestion = questionService.deleteById(question.getId());

            assertEquals(question, deletedQuestion);
        }
    }

    @Disabled
    @Test
    void createQuestion() {
        //todo implement after security
    }

    @Nested
    @DisplayName("close question")
    class closeQuestion{

        @Test
        @DisplayName(" - not existing question id")
        void closeQuestionWrongId(){
            given(questionRepository.findById(anyInt())).willReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> questionService.closeQuestion(1));
        }

        @Test
        @DisplayName(" - already closed")
        void closeQuestionAlreadyClosed(){
            Question question = Question.builder().id(1).title("title1").content("content1").closedAt(LocalDateTime.now()).build();
            given(questionRepository.findById(anyInt())).willReturn(Optional.of(question));

            assertThrows(QuestionAlreadyClosedException.class, () -> questionService.closeQuestion(1));
        }

        @Test
        @DisplayName(" - correct")
        void closeQuestionCorrect(){
            Question questionBefore = Question.builder().id(1).title("title1").content("content1").build();
            Question questionAfter = Question.builder().id(1).title("title1").content("content1").closedAt(LocalDateTime.now()).build();
            given(questionRepository.findById(anyInt())).willReturn(Optional.of(questionBefore));
            given(questionRepository.save(any(Question.class))).willReturn(questionAfter);

            Question foundQuestion = questionService.closeQuestion(1);

            assertNotNull(questionAfter.getClosedAt());
        }
    }

    @Nested
    @DisplayName("get questions by author")
    class getQuestionsByAuthor{

        @Nested
        @DisplayName(" - unpaged")
        class getQuestionsByAuthorUnpaged{

            @Test
            @DisplayName(" - not existing author username")
            void getQuestionsByAuthorWrongAuthorUsername() {
                given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, () -> questionService.getByAuthor("username"));
            }

            @Test
            @DisplayName(" - correct")
            void getQuestionsByAuthorCorrect() {
                Question question1 = Question.builder().id(1).title("title1").content("content1").build();
                Question question2 = Question.builder().id(2).title("title2").content("content2").build();
                List<Question> questions = List.of(question1, question2);
                given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
                given(questionRepository.findAllByAuthor(any(User.class))).willReturn(questions);

                List<Question> resultQuestions = questionService.getByAuthor("username");

                assertEquals(questions, resultQuestions);
            }
        }

        @Nested
        @DisplayName(" - paged")
        class getQuestionsByAuthorPaged{

            @Test
            @DisplayName(" - not existing author username")
            void getQuestionsByAuthorPagedWrongAuthorUsername() {
                given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, () -> questionService.getByAuthor("username", 1, 1, "sort"));
            }

            @Test
            @DisplayName(" - incorrect pagination arguments")
            void getQuestionsByAuthorPagedIncorrectPaginationArguments(){
                given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
                given(questionRepository.findAllByAuthor(any(), any())).willThrow(propertyReferenceException);

                assertThrows(IncorrectPageableException.class, () -> questionService.getByAuthor("username", 1, 1, "sort"));
            }

            @Test
            @DisplayName(" - correct")
            void getQuestionsByAuthorPagedCorrect() {
                Question question1 = Question.builder().id(1).title("title1").content("content1").build();
                Question question2 = Question.builder().id(2).title("title2").content("content2").build();
                Page<Question> questionPage = new PageImpl<>(List.of(question1, question2));
                given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
                given(questionRepository.findAllByAuthor(any(Pageable.class), any(User.class))).willReturn(questionPage);

                Page<Question> resultQuestionPage = questionService.getByAuthor("username", 1, 1, "sortBy");

                assertEquals(resultQuestionPage, questionPage);
            }
        }
    }

    @Disabled
    @Nested
    @DisplayName("get questions without best answer")
    class getQuestionsWithoutBestAnswer{

        @Nested
        @DisplayName(" - unpaged")
        class getQuestionsByAuthorUnpaged{
            //todo implement getQuestionsByAuthorUnpaged method test when getQuestionsByAuthorUnpaged gets more business logic
        }

        @Nested
        @DisplayName(" - paged")
        class getQuestionsByAuthorPaged{
            //todo implement getQuestionsByAuthorPaged method test when getQuestionsByAuthorPaged gets more business logic
        }
    }
}