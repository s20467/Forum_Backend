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
import spring.project.forum.model.PostAnswer;
import spring.project.forum.model.PostQuestion;
import spring.project.forum.model.security.User;
import spring.project.forum.repository.PostAnswerRepository;
import spring.project.forum.repository.PostQuestionRepository;
import spring.project.forum.repository.security.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostAnswerServiceImpl implements PostAnswerService{

    private final PostAnswerRepository answerRepository;
    private final PostQuestionRepository questionRepository;
    private final UserRepository userRepository;

    public PostAnswerServiceImpl(PostAnswerRepository answerRepository, PostQuestionRepository questionRepository, UserRepository userRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<PostAnswer> getAll() {
        return answerRepository.findAll();
    }

    @Override
    public Page<PostAnswer> getAll(Integer pageNum, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
        try {
            return answerRepository.findAll(pageable);
        }
        catch(PropertyReferenceException exc) {
            throw new IncorrectPageableException(exc.getMessage());
        }
    }

    @Override
    public PostAnswer getById(Integer answerId) {
        return answerRepository.findById(answerId).orElseThrow(
                () -> new ResourceNotFoundException("answer with id " + answerId + " not found")
        );
    }

    @Override
    public PostAnswer deleteById(Integer answerId) {
        Optional<PostAnswer> answerOptional = answerRepository.findById(answerId);
        if(answerOptional.isEmpty())
            throw new ResourceNotFoundException("answer with id " + answerId + "not found");
        answerRepository.deleteById(answerId);
        return answerOptional.get();
    }

    @Override
    public PostAnswer createAnswer(PostAnswer answer) {
        return answerRepository.save(answer);
    }

    @Override
    public List<PostAnswer> getByQuestion(Integer questionId) {
        PostQuestion foundQuestion = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question with id " + questionId + " not found"));
        return answerRepository.findAllByTargetQuestion(foundQuestion);
    }

    @Override
    public Page<PostAnswer> getByQuestion(Integer questionId, Integer pageNum, Integer pageSize, String sortBy) {
        PostQuestion foundQuestion = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question with id " + questionId + " not found"));
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
        try {
            return answerRepository.findAllByTargetQuestion(pageable, foundQuestion);
        }
        catch(PropertyReferenceException exc){
            throw new IncorrectPageableException(exc.getMessage());
        }
    }

    @Override
    public List<PostAnswer> getByAuthor(String username) {
        User foundUser = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));
        return answerRepository.findAllByAuthor(foundUser);
    }

    @Override
    public Page<PostAnswer> getByAuthor(String username, Integer pageNum, Integer pageSize, String sortBy) {
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
