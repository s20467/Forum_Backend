package spring.project.forum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import spring.project.forum.api.v1.dto.QuestionDto;
import spring.project.forum.api.v1.dto.QuestionDtoAdmin;
import spring.project.forum.api.v1.mapper.QuestionMapper;
import spring.project.forum.exception.*;
import spring.project.forum.model.Answer;
import spring.project.forum.model.Question;
import spring.project.forum.model.security.User;
import spring.project.forum.repository.AnswerRepository;
import spring.project.forum.repository.QuestionRepository;
import spring.project.forum.repository.security.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuestionMapper questionMapper;

    public QuestionServiceImpl(QuestionRepository questionRepository, AnswerRepository answerRepository, UserRepository userRepository, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.questionMapper = questionMapper;
    }

    @Override
    public Question getById(Integer questionId){
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if(questionOptional.isEmpty())
            throw new ResourceNotFoundException("Question with id " + questionId + " not found");
        return questionOptional.get();
    }

    @Override
    public void deleteById(Integer questionId) {
        if(!questionRepository.existsById(questionId))
            throw new ResourceNotFoundException("question with id " + questionId + "not found");
        questionRepository.deleteById(questionId);
    }

    @Override
    public Question updateQuestionAdmin(Integer questionId, QuestionDtoAdmin questionDtoAdmin) {
        Question updatedQuestion = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question with id " + questionId + " not found"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        updatedQuestion.setCreatedAt(questionDtoAdmin.getCreatedAt() == null ? null : LocalDate.parse(questionDtoAdmin.getCreatedAt(), formatter));
        updatedQuestion.setClosedAt(questionDtoAdmin.getClosedAt() == null ? null : LocalDate.parse(questionDtoAdmin.getClosedAt(), formatter));
        if(updatedQuestion.getClosedAt() != null && updatedQuestion.getCreatedAt().isAfter(updatedQuestion.getClosedAt()))
            throw new CustomValidationException("Question closing date cannot be before question creation date");
        updatedQuestion.setTitle(questionDtoAdmin.getTitle());
        updatedQuestion.setContent(questionDtoAdmin.getContent());
        updatedQuestion.setAuthor(userRepository.findByUsername(questionDtoAdmin.getAuthor()).get());
        return questionRepository.save(updatedQuestion);
    }

    @Override
    public Question updateQuestion(Integer questionId, QuestionDto questionDto) {
        Question updatedQuestion = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question with id " + questionId + " not found"));
        updatedQuestion.setTitle(questionDto.getTitle());
        updatedQuestion.setContent(questionDto.getContent());
        return questionRepository.save(updatedQuestion);
    }

    @Override
    public Question upVote(Integer questionId) {
        Question upVotedQuestion = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question with id " + questionId + " not found"));
        User upVoter = userRepository.findByUsername(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).get();
        if(upVotedQuestion.getUpVotes().contains(upVoter))
            throw new VotingException("User " + upVoter.getUsername() + " already upvoted question id " + upVotedQuestion.getId());
        if(upVotedQuestion.getDownVotes().contains(upVoter)){
            upVoter.getDownVotedQuestions().remove(upVotedQuestion);
            upVotedQuestion.getDownVotes().remove(upVoter);
        }
        upVoter.getUpVotedQuestions().add(upVotedQuestion);
        upVotedQuestion.getUpVotes().add(upVoter);
        return questionRepository.save(upVotedQuestion);
    }

    @Override
    public Question downVote(Integer questionId) {
        Question downVotedQuestion = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question with id " + questionId + " not found"));
        User downVoter = userRepository.findByUsername(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).get();
        if(downVotedQuestion.getDownVotes().contains(downVoter))
            throw new VotingException("User " + downVoter.getUsername() + " already downvoted question id " + downVotedQuestion.getId());
        if(downVotedQuestion.getUpVotes().contains(downVoter)){
            downVoter.getUpVotedQuestions().remove(downVotedQuestion);
            downVotedQuestion.getUpVotes().remove(downVoter);
        }
        downVoter.getDownVotedQuestions().add(downVotedQuestion);
        downVotedQuestion.getDownVotes().add(downVoter);
        return questionRepository.save(downVotedQuestion);
    }

    @Override
    public Question unUpVote(Integer questionId) {
        Question upVotedQuestion = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question with id " + questionId + " not found"));
        User upVoter = userRepository.findByUsername(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).get();
        if(!upVotedQuestion.getUpVotes().contains(upVoter))
            throw new VotingException("User " + upVoter.getUsername() + " haven't upvoted question id " + upVotedQuestion.getId());
        upVoter.getUpVotedQuestions().remove(upVotedQuestion);
        upVotedQuestion.getUpVotes().remove(upVoter);
        return questionRepository.save(upVotedQuestion);
    }

    @Override
    public Question unDownVote(Integer questionId) {
        Question downVotedQuestion = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question with id " + questionId + " not found"));
        User downVoter = userRepository.findByUsername(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).get();
        if(!downVotedQuestion.getDownVotes().contains(downVoter))
            throw new VotingException("User " + downVoter.getUsername() + " haven't downvoted question id " + downVotedQuestion.getId());
        downVoter.getDownVotedQuestions().remove(downVotedQuestion);
        downVotedQuestion.getDownVotes().remove(downVoter);
        return questionRepository.save(downVotedQuestion);
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
    @Transactional
    public Question createQuestionAdmin(QuestionDtoAdmin questionDtoAdmin) {
        Question newQuestion = questionMapper.questionDtoAdminToQuestion(questionDtoAdmin);
        if(newQuestion.getClosedAt() != null && newQuestion.getCreatedAt().isAfter(newQuestion.getClosedAt()))
            throw new CustomValidationException("Question closing date cannot be before question creation date");
        String authorUsername = questionDtoAdmin.getAuthor();
        newQuestion.setAuthor(userRepository.findByUsername(authorUsername).get());
        return questionRepository.save(newQuestion);
    }

    @Override
    @Transactional
    public Question createQuestion(QuestionDto questionDto) {
        Question newQuestion = questionMapper.questionDtoToQuestion(questionDto);
        User author = userRepository.findByUsername(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).get();
        newQuestion.setAuthor(author);
        newQuestion.setCreatedAt(LocalDate.now());
        author.getAskedQuestions().add(newQuestion);
        return questionRepository.save(newQuestion);
    }

    @Override
    public Question closeQuestion(Integer questionId){
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if(questionOptional.isEmpty())
            throw new ResourceNotFoundException("Question with id " + questionId + " not found");
        Question foundQuestion = questionOptional.get();
        if(foundQuestion.getClosedAt() != null)
            throw new QuestionAlreadyClosedException("Question with id " + questionId + " has already been closed at " + foundQuestion.getClosedAt());
        foundQuestion.setClosedAt(LocalDate.now());
        return questionRepository.save(foundQuestion);
    }

    @Override
    public Question openQuestion(Integer questionId) {
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if(questionOptional.isEmpty())
            throw new ResourceNotFoundException("Question with id " + questionId + " not found");
        Question foundQuestion = questionOptional.get();
        if(foundQuestion.getClosedAt() == null)
            throw new QuestionNotClosedException("Question with id " + questionId + " has already been closed at " + foundQuestion.getClosedAt());
        foundQuestion.setClosedAt(null);
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
    public List<Question> getQuestionsAnsweredByUser(String username) {
        User foundUser = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));
        return foundUser.getGivenAnswers().stream().map(Answer::getTargetQuestion).collect(Collectors.toList());
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
        foundQuestion.setBestAnswer(newBestAnswer);
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

    @Override
    public List<Question> getNotClosed() {
        return questionRepository.findAllByClosedAtIsNull();
    }
}
