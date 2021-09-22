package spring.project.forum.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import spring.project.forum.exception.ResourceNotFoundException;
import spring.project.forum.model.Answer;
import spring.project.forum.model.Question;
import spring.project.forum.model.security.User;
import spring.project.forum.repository.AnswerRepository;
import spring.project.forum.repository.QuestionRepository;
import spring.project.forum.repository.security.UserRepository;

@Component
public class CustomAuthenticationManager {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public CustomAuthenticationManager(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    public boolean isQuestionOwner(Authentication authentication, Integer questionId){
        Question foundQuestion = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question with id " + questionId + " not found"));
        User authenticatedUser = (User)authentication.getPrincipal();
        return foundQuestion.getAuthor().getId().equals(authenticatedUser.getId());
    }

    public boolean isAnswerOwner(Authentication authentication, Integer answerId){
        Answer foundAnswer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer with id " + answerId + " not found"));
        User authenticatedUser = (User)authentication.getPrincipal();
        return foundAnswer.getAuthor().getId().equals(authenticatedUser.getId());
    }
}
