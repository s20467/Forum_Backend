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
import spring.project.forum.exception.ResourceNotFoundException;
import spring.project.forum.model.Answer;
import spring.project.forum.model.Question;
import spring.project.forum.model.security.User;
import spring.project.forum.repository.AnswerRepository;
import spring.project.forum.repository.QuestionRepository;
import spring.project.forum.repository.security.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.*;

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

    @Nested
    @DisplayName("get all answers")
    class getAllAnswers{

        @Nested
        @DisplayName(" - unpaged")
        class getAllAnswersUnpaged{

            @Test
            @DisplayName(" - correct")
            void getAllAnswersCorrect(){
                Answer answer1 = Answer.builder().id(1).content("content1").build();
                Answer answer2 = Answer.builder().id(2).content("content2").build();
                List<Answer> answers = List.of(answer1, answer2);
                given(answerRepository.findAll()).willReturn(answers);

                List<Answer> foundAnswers = answerService.getAll();

                assertEquals(answers, foundAnswers);
            }
        }

        @Nested
        @DisplayName(" - paged")
        class getAllAnswersPaged{

            @Test
            @DisplayName(" - incorrect pagination arguments")
            void getAllAnswersPagedIncorrectPaginationArguments(){
                given(answerRepository.findAll(any(Pageable.class))).willThrow(propertyReferenceException);

                assertThrows(IncorrectPageableException.class, () -> answerService.getAll(1, 1, "sort"));
            }

            @Test
            @DisplayName(" - correct")
            void getAllAnswersPagedCorrect(){
                Answer answer1 = Answer.builder().id(1).content("content1").build();
                Answer answer2 = Answer.builder().id(2).content("content2").build();
                Page<Answer> answers = new PageImpl<>(List.of(answer1, answer2));
                given(answerRepository.findAll(any(Pageable.class))).willReturn(answers);

                Page<Answer> foundAnswers = answerService.getAll(1, 1, "sort");

                assertEquals(answers, foundAnswers);
            }
        }
    }

    @Nested
    @DisplayName("get answer by id")
    class getAnswerById{

        @Test
        @DisplayName(" - not existing answer id")
        void getAnswerByIdWrongId(){
            given(answerRepository.findById(anyInt())).willReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> answerService.getById(1));
        }

        @Test
        @DisplayName(" - correct")
        void getAnswerByIdCorrect(){
            Answer answer = Answer.builder().id(1).content("content1").build();
            given(answerRepository.findById(anyInt())).willReturn(Optional.of(answer));

            Answer foundAnswer = answerService.getById(answer.getId());

            assertEquals(answer, foundAnswer);
        }
    }

    @Nested
    @DisplayName("delete answer by id")
    class deleteAnswerById{

        @Test
        @DisplayName(" - not existing answer id")
        void deleteAnswerByIdWrongId(){
            given(answerRepository.findById(anyInt())).willReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> answerService.deleteById(1));
        }

        @Test
        @DisplayName(" - correct")
        void deleteAnswerByIdCorrect(){
            Answer answer = Answer.builder().id(1).content("content1").build();
            given(answerRepository.findById(anyInt())).willReturn(Optional.of(answer));

            Answer deletedAnswer = answerService.deleteById(answer.getId());

            assertEquals(answer, deletedAnswer);
        }
    }

    @Disabled
    @Nested
    @DisplayName("create answer")
    class createAnswer{
        //todo implement createAnswer method test when createAnswer gets more business logic
    }

    @Nested
    @DisplayName("get answers by question")
    class getAnswersByQuestion{

        @Nested
        @DisplayName(" - unpaged")
        class getAnswersByQuestionUnpaged{

            @Test
            @DisplayName(" - not existing question id")
            void getAnswersByQuestionWrongQuestionId() {
                given(questionRepository.findById(anyInt())).willReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, () -> answerService.getByQuestion(1));
            }

            @Test
            @DisplayName(" - correct")
            void getAnswersByQuestionCorrect(){
                Answer answer = Answer.builder().id(1).content("answer").build();
                Page<Answer> answerPage = new PageImpl<>(List.of(answer));
                given(questionRepository.findById(anyInt())).willReturn(Optional.of(new Question()));
                given(answerRepository.findAllByTargetQuestion(any(Pageable.class), any(Question.class))).willReturn(answerPage);

                Page<Answer> resultAnswerPage = answerService.getByQuestion(1, 1, 1, "sort");

                assertEquals(resultAnswerPage, answerPage);
            }
        }

        @Nested
        @DisplayName(" - paged")
        class getAnswersByQuestionPaged{

            @Test
            @DisplayName(" - not existing question id")
            void getAnswersByQuestionPagedWrongQuestionId() {
                given(questionRepository.findById(anyInt())).willReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, () -> answerService.getByQuestion(1, 1, 1, "sort"));
            }

            @Test
            @DisplayName(" - incorrect pagination arguments")
            void getAnswersByQuestionPagedIncorrectPaginationArguments(){
                given(questionRepository.findById(anyInt())).willReturn(Optional.of(new Question()));
                given(answerRepository.findAllByTargetQuestion(any(), any())).willThrow(propertyReferenceException);

                assertThrows(IncorrectPageableException.class, () -> answerService.getByQuestion(1, 1, 1, "sort"));
            }

            @Test
            @DisplayName(" - correct")
            void getAnswersByQuestionPagedCorrect(){
                Answer answer = Answer.builder().id(1).content("answer").build();
                Page<Answer> answerPage = new PageImpl<>(List.of(answer));
                given(questionRepository.findById(anyInt())).willReturn(Optional.of(new Question()));
                given(answerRepository.findAllByTargetQuestion(any(Pageable.class), any(Question.class))).willReturn(answerPage);

                Page<Answer> resultAnswerPage = answerService.getByQuestion(1, 1, 1, "sort");

                assertEquals(resultAnswerPage, answerPage);
            }
        }
    }


    @Nested
    @DisplayName("get answers by author")
    class getAnswersByAuthor{

        @Nested
        @DisplayName(" - unpaged")
        class getAnswersByAuthorUnpaged{

            @Test
            @DisplayName(" - not existing author username")
            void getAnswersByAuthorWrongAuthorUsername() {
                given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, () -> answerService.getByAuthor("username"));
            }

            @Test
            @DisplayName(" - correct")
            void getAnswersByAuthorCorrect() {
                Answer answer1 = Answer.builder().id(1).content("content1").build();
                Answer answer2 = Answer.builder().id(2).content("content2").build();
                List<Answer> answers = List.of(answer1, answer2);
                given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
                given(answerRepository.findAllByAuthor(any(User.class))).willReturn(answers);

                List<Answer> resultAnswers = answerService.getByAuthor("username");

                assertEquals(resultAnswers, answers);
            }
        }

        @Nested
        @DisplayName(" - paged")
        class getAnswersByAuthorPaged{

            @Test
            @DisplayName(" - not existing author username")
            void getAnswersByAuthorPagedWrongAuthorUsername() {
                given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, () -> answerService.getByAuthor("username", 1, 1, "sort"));
            }

            @Test
            @DisplayName(" - incorrect pagination arguments")
            void getAnswersByAuthorPagedIncorrectPaginationArguments(){
                given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
                given(answerRepository.findAllByAuthor(any(), any())).willThrow(propertyReferenceException);

                assertThrows(IncorrectPageableException.class, () -> answerService.getByAuthor("username", 1, 1, "sort"));
            }

            @Test
            @DisplayName(" - correct")
            void getAnswersByAuthorPagedCorrect() {
                Answer answer1 = Answer.builder().id(1).content("content1").build();
                Answer answer2 = Answer.builder().id(2).content("content2").build();
                Page<Answer> answerPage = new PageImpl<>(List.of(answer1, answer2));
                given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
                given(answerRepository.findAllByAuthor(any(Pageable.class), any(User.class))).willReturn(answerPage);

                Page<Answer> resultAnswerPage = answerService.getByAuthor("username", 1, 1, "sortBy");

                assertEquals(answerPage, resultAnswerPage);
            }
        }
    }
}