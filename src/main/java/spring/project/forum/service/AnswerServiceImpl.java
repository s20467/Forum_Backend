package spring.project.forum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
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

@Service
@Slf4j
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository, QuestionRepository questionRepository, UserRepository userRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Answer> getAll() {
        return answerRepository.findAll();
    }

    @Override
    public Page<Answer> getAll(Integer pageNum, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
        try {
            return answerRepository.findAll(pageable);
        }
        catch(PropertyReferenceException exc) {
            throw new IncorrectPageableException(exc.getMessage());
        }
    }

    @Override
    public Answer getById(Integer answerId) {
        return answerRepository.findById(answerId).orElseThrow(
                () -> new ResourceNotFoundException("answer with id " + answerId + " not found")
        );
    }

    @Override
    public Answer deleteById(Integer answerId) {
        Optional<Answer> answerOptional = answerRepository.findById(answerId);
        if(answerOptional.isEmpty())
            throw new ResourceNotFoundException("answer with id " + answerId + "not found");
        answerRepository.deleteById(answerId);
        return answerOptional.get();
    }

    @Override
    public Answer createAnswer(Answer answer) {
        return answerRepository.save(
                Answer.builder()
                        .content(answer.getContent())
                        .author(null)//todo change author after security
                        .build()
        );
    }

    @Override
    public Answer updateAnswerContent(Integer answerId, Answer answer) {
        Answer updatedAnswer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer with id " + answerId + " not found"));
        updatedAnswer.setContent(answer.getContent());
        return answerRepository.save(updatedAnswer);
    }

    @Override
    public Answer setAsBestAnswer(Integer answerId) {
        Answer updatedAnswer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer with id " + answerId + " not found"));
        updatedAnswer.setIsBestAnswer(true);
        return answerRepository.save(updatedAnswer);
    }

    @Override
    public Answer unsetAsBestAnswer(Integer answerId) {
        Answer updatedAnswer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer with id " + answerId + " not found"));
        updatedAnswer.setIsBestAnswer(false);
        return answerRepository.save(updatedAnswer);
    }

    @Override
    public Answer upVote(Integer answerId) {
        //todo implement after security
        return null;
    }

    @Override
    public Answer downVote(Integer answerId) {
        //todo implement after security
        return null;
    }

    @Override
    public List<Answer> getByQuestion(Integer questionId) {
        Question foundQuestion = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question with id " + questionId + " not found"));
        return answerRepository.findAllByTargetQuestion(foundQuestion);
    }

    @Override
    public Page<Answer> getByQuestion(Integer questionId, Integer pageNum, Integer pageSize, String sortBy) {
        Question foundQuestion = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question with id " + questionId + " not found"));
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
        try {
            return answerRepository.findAllByTargetQuestion(pageable, foundQuestion);
        }
        catch(PropertyReferenceException exc){
            throw new IncorrectPageableException(exc.getMessage());
        }
    }

    @Override
    public List<Answer> getByAuthor(String username) {
        User foundUser = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));
        return answerRepository.findAllByAuthor(foundUser);
    }

    @Override
    public Page<Answer> getByAuthor(String username, Integer pageNum, Integer pageSize, String sortBy) {
        User foundUser = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
        try {
            return answerRepository.findAllByAuthor(pageable, foundUser);
        }
        catch(PropertyReferenceException exc) {
            throw new IncorrectPageableException(exc.getMessage());
        }
    }
}
