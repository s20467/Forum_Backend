package spring.project.forum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import spring.project.forum.api.v1.dto.AnswerDto;
import spring.project.forum.api.v1.dto.AnswerDtoAdmin;
import spring.project.forum.api.v1.mapper.AnswerMapper;
import spring.project.forum.exception.VotingException;
import spring.project.forum.exception.IncorrectPageableException;
import spring.project.forum.exception.ResourceNotFoundException;
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

@Service
@Slf4j
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AnswerMapper answerMapper;

    public AnswerServiceImpl(AnswerRepository answerRepository, QuestionRepository questionRepository, UserRepository userRepository, AnswerMapper answerMapper) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.answerMapper = answerMapper;
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
    @Transactional
    public void deleteById(Integer answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("answer with id " + answerId + "not found"));
        if(answer.getIsBestAnswer()) {
            Question targetQuestion = answer.getTargetQuestion();
            targetQuestion.setBestAnswer(null);
            questionRepository.save(targetQuestion);
        }
        answerRepository.deleteById(answerId);
    }

    @Override
    @Transactional
    public Answer createAnswerForQuestionAdmin(Integer questionId, AnswerDtoAdmin answerDtoAdmin) {
        String authorUsername = answerDtoAdmin.getAuthor();
        Question targetQuestion = questionRepository.getById(questionId);
        Answer newAnswer = answerMapper.answerDtoAdminToAnswer(answerDtoAdmin);
        newAnswer.setTargetQuestion(targetQuestion);
        newAnswer.setAuthor(userRepository.findByUsername(authorUsername).get());
        return answerRepository.save(newAnswer);
    }

    @Override
    @Transactional
    public Answer createAnswerForQuestion(Integer questionId, AnswerDto answerDto) {
        User author = userRepository.findByUsername(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).get();
        Question targetQuestion = questionRepository.getById(questionId);
        Answer newAnswer = answerMapper.answerDtoToAnswer(answerDto);
        newAnswer.setAuthor(author);
        newAnswer.setTargetQuestion(targetQuestion);
        newAnswer.setCreatedAt(LocalDate.now());
        author.getGivenAnswers().add(newAnswer);
        return answerRepository.save(newAnswer);
    }

    @Override
    public Answer updateAnswerAdmin(Integer answerId, AnswerDtoAdmin answerDtoAdmin) {
        Answer updatedAnswer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer with id " + answerId + " not found"));
        updatedAnswer.setContent(answerDtoAdmin.getContent());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        updatedAnswer.setCreatedAt(answerDtoAdmin.getCreatedAt() == null ? null : LocalDate.parse(answerDtoAdmin.getCreatedAt(), formatter));
        updatedAnswer.setAuthor(userRepository.findByUsername(answerDtoAdmin.getAuthor()).get());
        return answerRepository.save(updatedAnswer);
    }

    @Override
    public Answer updateAnswer(Integer answerId, AnswerDto answerDto) {
        Answer updatedAnswer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer with id " + answerId + " not found"));
        updatedAnswer.setContent(answerDto.getContent());
        return answerRepository.save(updatedAnswer);
    }

    @Override
    public Answer upVote(Integer answerId) {
        Answer upVotedAnswer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer with id " + answerId + " not found"));
        User upVoter = userRepository.findByUsername(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).get();
        if(upVotedAnswer.getUpVotes().contains(upVoter))
            throw new VotingException("User " + upVoter.getUsername() + " already upvoted answer id " + upVotedAnswer.getId());
        if(upVotedAnswer.getDownVotes().contains(upVoter)){
            upVoter.getDownVotedAnswers().remove(upVotedAnswer);
            upVotedAnswer.getDownVotes().remove(upVoter);
        }
        upVoter.getUpVotedAnswers().add(upVotedAnswer);
        upVotedAnswer.getUpVotes().add(upVoter);
        return answerRepository.save(upVotedAnswer);
    }

    @Override
    public Answer downVote(Integer answerId) {
        Answer downVotedAnswer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer with id " + answerId + " not found"));
        User downVoter = userRepository.findByUsername(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).get();
        if(downVotedAnswer.getDownVotes().contains(downVoter))
            throw new VotingException("User " + downVoter.getUsername() + " already downvoted answer id " + downVotedAnswer.getId());
        if(downVotedAnswer.getUpVotes().contains(downVoter)){
            downVoter.getUpVotedAnswers().remove(downVotedAnswer);
            downVotedAnswer.getUpVotes().remove(downVoter);
        }
        downVoter.getDownVotedAnswers().add(downVotedAnswer);
        downVotedAnswer.getDownVotes().add(downVoter);
        return answerRepository.save(downVotedAnswer);
    }

    @Override
    public Answer unUpVote(Integer answerId) {
        Answer upVotedAnswer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer with id " + answerId + " not found"));
        User upVoter = userRepository.findByUsername(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).get();
        if(!upVotedAnswer.getUpVotes().contains(upVoter))
            throw new VotingException("User " + upVoter.getUsername() + " haven't upvoted answer id " + upVotedAnswer.getId());
        upVoter.getUpVotedAnswers().remove(upVotedAnswer);
        upVotedAnswer.getUpVotes().remove(upVoter);
        return answerRepository.save(upVotedAnswer);
    }

    @Override
    public Answer unDownVote(Integer answerId) {
        Answer downVotedAnswer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer with id " + answerId + " not found"));
        User downVoter = userRepository.findByUsername(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).get();
        if(!downVotedAnswer.getDownVotes().contains(downVoter))
            throw new VotingException("User " + downVoter.getUsername() + " haven't downvoted answer id " + downVotedAnswer.getId());
        downVoter.getDownVotedAnswers().remove(downVotedAnswer);
        downVotedAnswer.getDownVotes().remove(downVoter);
        return answerRepository.save(downVotedAnswer);
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
