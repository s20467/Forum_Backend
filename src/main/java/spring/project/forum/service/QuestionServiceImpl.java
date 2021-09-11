package spring.project.forum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import spring.project.forum.exception.IncorrectPageableException;
import spring.project.forum.exception.QuestionAlreadyClosedException;
import spring.project.forum.exception.ResourceNotFoundException;
import spring.project.forum.model.Answer;
import spring.project.forum.model.Question;
import spring.project.forum.model.security.User;
import spring.project.forum.repository.AnswerRepository;
import spring.project.forum.repository.QuestionRepository;
import spring.project.forum.repository.security.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, AnswerRepository answerRepository, UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Question getById(Integer questionId){
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if(questionOptional.isEmpty())
            throw new ResourceNotFoundException("Question with id " + questionId + " not found");
        return questionOptional.get();
    }

    @Override
    public Question deleteById(Integer questionId) {
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if(questionOptional.isEmpty())
            throw new ResourceNotFoundException("question with id " + questionId + "not found");
        questionRepository.deleteById(questionId);
        return questionOptional.get();
    }

    @Override
    public Question updateQuestion(Integer questionId, Question question) {
        Question updatedQuestion = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question with id " + questionId + " not found"));
        updatedQuestion.setTitle(question.getTitle());
        updatedQuestion.setContent(question.getContent());
        return questionRepository.save(updatedQuestion);
    }

    @Override
    public Question upVote(Integer questionId) {
        //todo implement after security
        return null;
    }

    @Override
    public Question downVote(Integer questionId) {
        //todo implement after security
        return null;
    }

    @Override
    public List<Question> getAll() {
        return questionRepository.findAll();
    }

    @Override
    public Page<Question> getAll(Integer pageNum, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
        try {
            return questionRepository.findAll(pageable);
        }
        catch(PropertyReferenceException exc){
            throw new IncorrectPageableException(exc.getMessage());
        }
    }

    @Override
    public Question createQuestion(Question question) {
        question.setClosedAt(null);
        question.setAnswers(null);
        question.setDownVotes(null);
        question.setBestAnswer(null);
        question.setAuthor(null); //todo after security implementation change to logged user
        return questionRepository.save(question);
    }

    @Override
    public Question closeQuestion(Integer questionId){
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if(questionOptional.isEmpty())
            throw new ResourceNotFoundException("Question with id " + questionId + " not found");
        Question foundQuestion = questionOptional.get();
        if(foundQuestion.getClosedAt() != null)
            throw new QuestionAlreadyClosedException("Question with id " + questionId + " has already been closed at " + foundQuestion.getClosedAt());
        foundQuestion.setClosedAt(LocalDateTime.now());
        return questionRepository.save(foundQuestion);
    }

    @Override
    public List<Question> getByAuthor(String username) {
        User foundUser = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));
        return questionRepository.findAllByAuthor(foundUser);
    }

    @Override
    public Page<Question> getByAuthor(String username, Integer pageNum, Integer pageSize, String sortBy) {
        User foundUser = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
        try {
            return questionRepository.findAllByAuthor(pageable, foundUser);
        }
        catch(PropertyReferenceException exc) {
            throw new IncorrectPageableException(exc.getMessage());
        }
    }

    @Override
    public List<Question> getWithoutBestAnswer(String username) {
        return questionRepository.findAllByBestAnswerIsNull();
    }

    @Override
    public Page<Question> getWithoutBestAnswer(String username, Integer pageNum, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
        try {
            return questionRepository.findAllByBestAnswerIsNull(pageable);
        }
        catch(PropertyReferenceException exc) {
            throw new IncorrectPageableException(exc.getMessage());
        }
    }

    @Override
    @Transactional
    public Question setBestAnswer(Integer questionId, Integer answerId) {
        Question foundQuestion = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question with id " + questionId + " not found"));
        Answer newBestAnswer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer with id " + answerId + " not found"));
        Answer oldBestAnswer = foundQuestion.getBestAnswer();
        oldBestAnswer.setIsBestAnswer(false);
        newBestAnswer.setIsBestAnswer(true);
        answerRepository.save(oldBestAnswer);
        return questionRepository.save(foundQuestion);
    }

    @Override
    @Transactional
    public Question unsetBestAnswer(Integer questionId) {
        Question foundQuestion = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question with id " + questionId + " not found"));
        Answer bestAnswer = foundQuestion.getBestAnswer();
        foundQuestion.setBestAnswer(null);
        bestAnswer.setIsBestAnswer(false);
        answerRepository.save(bestAnswer);
        return questionRepository.save(foundQuestion);
    }
}
