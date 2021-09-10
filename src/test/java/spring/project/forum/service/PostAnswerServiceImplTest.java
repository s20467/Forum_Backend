package spring.project.forum.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import spring.project.forum.exception.IncorrectPageableException;
import spring.project.forum.exception.ResourceNotFoundException;
import spring.project.forum.model.PostAnswer;
import spring.project.forum.model.PostQuestion;
import spring.project.forum.model.security.User;
import spring.project.forum.repository.PostAnswerRepository;
import spring.project.forum.repository.PostQuestionRepository;
import spring.project.forum.repository.security.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PostAnswerServiceImplTest {

    @Mock
    PostQuestionRepository questionRepository;
    @Mock
    PostAnswerRepository answerRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    PropertyReferenceException propertyReferenceException;

    @InjectMocks
    PostAnswerServiceImpl answerService;

    @Nested
    @DisplayName("get all answers")
    class getAllAnswers{

        @Nested
        @DisplayName(" - unpaged")
        class getAllAnswersUnpaged{

            @Test
            @DisplayName(" - correct")
            void getAllAnswersCorrect(){
                PostAnswer answer1 = PostAnswer.builder().id(1).content("content1").build();
                PostAnswer answer2 = PostAnswer.builder().id(2).content("content2").build();
                List<PostAnswer> answers = List.of(answer1, answer2);
                given(answerRepository.findAll()).willReturn(answers);

                List<PostAnswer> foundAnswers = answerService.getAll();

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
                PostAnswer answer1 = PostAnswer.builder().id(1).content("content1").build();
                PostAnswer answer2 = PostAnswer.builder().id(2).content("content2").build();
                Page<PostAnswer> answers = new PageImpl<>(List.of(answer1, answer2));
                given(answerRepository.findAll(any(Pageable.class))).willReturn(answers);

                Page<PostAnswer> foundAnswers = answerService.getAll(1, 1, "sort");

                assertEquals(answers, foundAnswers);
            }
        }

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

                assertThrows(ResourceNotFoundException.class, () -> answerService.getAnswersByQuestion(1));
            }

            @Test
            @DisplayName(" - correct")
            void getAnswersByQuestionCorrect(){
                PostAnswer answer = PostAnswer.builder().id(1).content("answer").build();
                Page<PostAnswer> answerPage = new PageImpl<>(List.of(answer));
                given(questionRepository.findById(anyInt())).willReturn(Optional.of(new PostQuestion()));
                given(answerRepository.findAllByTargetQuestion(any(Pageable.class), any(PostQuestion.class))).willReturn(answerPage);

                Page<PostAnswer> resultAnswerPage = answerService.getAnswersByQuestion(1, 1, 1, "sort");

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

                assertThrows(ResourceNotFoundException.class, () -> answerService.getAnswersByQuestion(1, 1, 1, "sort"));
            }

            @Test
            @DisplayName(" - incorrect pagination arguments")
            void getAnswersByQuestionPagedIncorrectPaginationArguments(){
                given(questionRepository.findById(anyInt())).willReturn(Optional.of(new PostQuestion()));
                given(answerRepository.findAllByTargetQuestion(any(), any())).willThrow(propertyReferenceException);

                assertThrows(IncorrectPageableException.class, () -> answerService.getAnswersByQuestion(1, 1, 1, "sort"));
            }

            @Test
            @DisplayName(" - correct")
            void getAnswersByQuestionPagedCorrect(){
                PostAnswer answer = PostAnswer.builder().id(1).content("answer").build();
                Page<PostAnswer> answerPage = new PageImpl<>(List.of(answer));
                given(questionRepository.findById(anyInt())).willReturn(Optional.of(new PostQuestion()));
                given(answerRepository.findAllByTargetQuestion(any(Pageable.class), any(PostQuestion.class))).willReturn(answerPage);

                Page<PostAnswer> resultAnswerPage = answerService.getAnswersByQuestion(1, 1, 1, "sort");

                assertEquals(resultAnswerPage, answerPage);
            }
        }
    }


    @Nested
    @DisplayName("get answers by question")
    class getAnswersByAuthor{

        @Nested
        @DisplayName(" - unpaged")
        class getAnswersByAuthorUnpaged{

            @Test
            @DisplayName(" - not existing author username")
            void getAnswersByAuthorWrongAuthorUsername() {
                given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());

                assertThrows(ResourceNotFoundException.class, () -> answerService.getAnswersByAuthor("username"));
            }

            @Test
            @DisplayName(" - correct")
            void getAnswersByAuthorCorrect() {
                PostAnswer answer1 = PostAnswer.builder().id(1).content("content1").build();
                PostAnswer answer2 = PostAnswer.builder().id(2).content("content2").build();
                List<PostAnswer> answers = List.of(answer1, answer2);
                given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
                given(answerRepository.findAllByAuthor(any(User.class))).willReturn(answers);

                List<PostAnswer> resultAnswers = answerService.getAnswersByAuthor("username");

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

                assertThrows(ResourceNotFoundException.class, () -> answerService.getAnswersByAuthor("username", 1, 1, "sort"));
            }

            @Test
            @DisplayName(" - incorrect pagination arguments")
            void getAnswersByAuthorPagedIncorrectPaginationArguments(){
                given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
                given(answerRepository.findAllByAuthor(any(), any())).willThrow(propertyReferenceException);

                assertThrows(IncorrectPageableException.class, () -> answerService.getAnswersByAuthor("username", 1, 1, "sort"));
            }

            @Test
            @DisplayName(" - correct")
            void getAnswersByAuthorPagedCorrect() {
                PostAnswer answer = PostAnswer.builder().id(1).content("answer").build();
                Page<PostAnswer> answerPage = new PageImpl<>(List.of(answer));
                given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));
                given(answerRepository.findAllByAuthor(any(Pageable.class), any(User.class))).willReturn(answerPage);

                Page<PostAnswer> resultAnswerPage = answerService.getAnswersByAuthor("username", 1, 1, "sortBy");

                assertEquals(resultAnswerPage, answerPage);
            }
        }
    }
}