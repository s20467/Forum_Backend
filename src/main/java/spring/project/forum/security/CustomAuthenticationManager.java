package spring.project.forum.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import spring.project.forum.exception.ResourceNotFoundException;
import spring.project.forum.model.Answer;
import spring.project.forum.model.Question;
import spring.project.forum.model.security.User;
import spring.project.forum.repository.AnswerRepository;
import spring.project.forum.repository.QuestionRepository;

@Component
public class CustomAuthenticationManager {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public CustomAuthenticationManager(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    public boolean isQuestionOwner(Authentication authentication, Integer questionId) {
        Question foundQuestion = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question with id " + questionId + " not found"));
        User authenticatedUser = (User) authentication.getPrincipal();
        return foundQuestion.getAuthor().getUsername().equals(authenticatedUser.getUsername());
    }

    public boolean isAnswerOwner(Authentication authentication, Integer answerId) {
        Answer foundAnswer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer with id " + answerId + " not found"));
        User authenticatedUser = (User) authentication.getPrincipal();
        return foundAnswer.getAuthor().getUsername().equals(authenticatedUser.getUsername());
    }

    public boolean isAccountOwner(Authentication authentication, String username) {
        User authenticatedUser = (User) authentication.getPrincipal();
        return username.equals(authenticatedUser.getUsername());
    }
}
