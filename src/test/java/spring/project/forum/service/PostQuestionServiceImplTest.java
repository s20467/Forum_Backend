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
import spring.project.forum.exception.PostQuestionAlreadyClosedException;
import spring.project.forum.exception.ResourceNotFoundException;
import spring.project.forum.model.PostAnswer;
import spring.project.forum.model.PostQuestion;
import spring.project.forum.model.security.User;
import spring.project.forum.repository.PostAnswerRepository;
import spring.project.forum.repository.PostQuestionRepository;
import spring.project.forum.repository.security.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostQuestionServiceImplTest {

    @Mock
    PostQuestionRepository questionRepository;
    @Mock
    PostAnswerRepository answerRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    PropertyReferenceException propertyReferenceException;

    @InjectMocks
    PostQuestionServiceImpl questionService;

    @Nested
    @DisplayName("get all questions")
    class getAllQuestions{

        @Nested
        @DisplayName(" - unpaged")
        class getAllQuestionsUnpaged{

            @Test
            @DisplayName(" - correct")
            void getAllQuestionsCorrect(){
                PostQuestion question1 = PostQuestion.builder().id(1).title("title1").content("content1").build();
                PostQuestion question2 = PostQuestion.builder().id(2).title("title2").content("content2").build();
                List<PostQuestion> questions = List.of(question1, question2);
                given(questionRepository.findAll()).willReturn(questions);

                List<PostQuestion> foundQuestions = questionService.getAll();

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
                PostQuestion question1 = PostQuestion.builder().id(1).title("title1").content("content1").build();
                PostQuestion question2 = PostQuestion.builder().id(2).title("title2").content("content2").build();
                Page<PostQuestion> questions = new PageImpl<>(List.of(question1, question2));
                given(questionRepository.findAll(any(Pageable.class))).willReturn(questions);

                Page<PostQuestion> foundQuestions = questionService.getAll(1, 1, "sort");

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
            PostQuestion question = PostQuestion.builder().id(1).title("title1").content("content1").build();
            given(questionRepository.findById(anyInt())).willReturn(Optional.of(question));

            PostQuestion foundQuestion = questionService.getById(question.getId());

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
            PostQuestion question = PostQuestion.builder().id(1).title("title1").content("content1").build();
            given(questionRepository.findById(anyInt())).willReturn(Optional.of(question));

            PostQuestion deletedQuestion = questionService.deleteById(question.getId());

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
            PostQuestion question = PostQuestion.builder().id(1).title("title1").content("content1").closedAt(LocalDateTime.now()).build();
            given(questionRepository.findById(anyInt())).willReturn(Optional.of(question));

            assertThrows(PostQuestionAlreadyClosedException.class, () -> questionService.closeQuestion(1));
        }

        @Test
        @DisplayName(" - correct")
        void closeQuestionCorrect(){
            PostQuestion questionBefore = PostQuestion.builder().id(1).title("title1").content("content1").build();
            PostQuestion questionAfter = PostQuestion.builder().id(1).title("title1").content("content1").closedAt(LocalDateTime.now()).build();
            given(questionRepository.findById(anyInt())).willReturn(Optional.of(questionBefore));
            given(questionRepository.save(any(PostQuestion.class))).willReturn(questionAfter);

            PostQuestion foundQuestion = questionService.closeQuestion(1);

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
                PostQuestion question1 = PostQuestion.builder().id(1).title("title1").content("content1").build();
                PostQuestion question2 = PostQuestion.builder().id(2).title("title2").content("content2").build();
                List<PostQuestion> questions = List.of(question1, question2);
                given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
                given(questionRepository.findAllByAuthor(any(User.class))).willReturn(questions);

                List<PostQuestion> resultQuestions = questionService.getByAuthor("username");

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
                PostQuestion question1 = PostQuestion.builder().id(1).title("title1").content("content1").build();
                PostQuestion question2 = PostQuestion.builder().id(2).title("title2").content("content2").build();
                Page<PostQuestion> questionPage = new PageImpl<>(List.of(question1, question2));
                given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
                given(questionRepository.findAllByAuthor(any(Pageable.class), any(User.class))).willReturn(questionPage);

                Page<PostQuestion> resultQuestionPage = questionService.getByAuthor("username", 1, 1, "sortBy");

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