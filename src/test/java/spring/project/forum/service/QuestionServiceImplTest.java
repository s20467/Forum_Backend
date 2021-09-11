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

    Question question1;
    Question question2;
    Question closedQuestion1;
    List<Question> listOfQuestions;
    Page<Question> pageOfQuestions;

    @BeforeEach
    void setUp(){
        question1 = Question.builder().id(1).title("title1").content("content1").build();
        question2 = Question.builder().id(2).title("title2").content("content2").build();
        closedQuestion1 = Question.builder().id(1).title("title1").content("content1").closedAt(LocalDateTime.now()).build();
        listOfQuestions = List.of(question1, question2);
        pageOfQuestions = new PageImpl<>(listOfQuestions);
    }

    @Nested
    @DisplayName("get all questions")
    class getAllQuestions{

        @Nested
        @DisplayName(" - unpaged")
        class getAllQuestionsUnpaged{

            @Test
            @DisplayName(" - correct")
            void getAllQuestionsCorrect(){
                given(questionRepository.findAll()).willReturn(listOfQuestions);

                List<Question> foundQuestions = questionService.getAll();

                assertEquals(listOfQuestions, foundQuestions);
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
                given(questionRepository.findAll(any(Pageable.class))).willReturn(pageOfQuestions);

                Page<Question> foundQuestions = questionService.getAll(1, 1, "sort");

                assertEquals(pageOfQuestions, foundQuestions);
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
            given(questionRepository.findById(anyInt())).willReturn(Optional.of(question1));

            Question foundQuestion = questionService.getById(question1.getId());

            assertEquals(question1, foundQuestion);
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
            given(questionRepository.findById(anyInt())).willReturn(Optional.of(question1));

            Question deletedQuestion = questionService.deleteById(question1.getId());

            assertEquals(question1, deletedQuestion);
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
            given(questionRepository.findById(anyInt())).willReturn(Optional.of(closedQuestion1));

            assertThrows(QuestionAlreadyClosedException.class, () -> questionService.closeQuestion(1));
        }

        @Test
        @DisplayName(" - correct")
        void closeQuestionCorrect(){
            given(questionRepository.findById(anyInt())).willReturn(Optional.of(question1));
            given(questionRepository.save(any(Question.class))).willReturn(closedQuestion1);

            Question foundQuestion = questionService.closeQuestion(1);

            assertNotNull(closedQuestion1.getClosedAt());
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
                given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
                given(questionRepository.findAllByAuthor(any(User.class))).willReturn(listOfQuestions);

                List<Question> resultQuestions = questionService.getByAuthor("username");

                assertEquals(listOfQuestions, resultQuestions);
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
                given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
                given(questionRepository.findAllByAuthor(any(Pageable.class), any(User.class))).willReturn(pageOfQuestions);

                Page<Question> resultQuestionPage = questionService.getByAuthor("username", 1, 1, "sortBy");

                assertEquals(pageOfQuestions, resultQuestionPage);
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