package spring.project.forum.api.v1.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.project.forum.api.v1.dto.AnswerDto;
import spring.project.forum.api.v1.dto.QuestionDto;
import spring.project.forum.model.Answer;
import spring.project.forum.model.Question;
import spring.project.forum.service.AnswerService;
import spring.project.forum.service.QuestionService;

import java.util.List;

@RestController
@RequestMapping("api")
public class PostController {

    private final QuestionService questionService;
    private final AnswerService answerService;


    public PostController(QuestionService questionService, AnswerService answerService) {
        this.questionService = questionService;
        this.answerService = answerService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("questions/{questionId}")
    public Question getQuestionById(@PathVariable("questionId") Integer questionId) {
        return questionService.getById(questionId);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("questions")
    public List<Question> getQuestions() {
        return questionService.getAll();
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "questions", params = {"page", "limit", "sort"})
    public Page<Question> getQuestions(
            @RequestParam("page") Integer pageNum,
            @RequestParam("limit") Integer pageSize,
            @RequestParam("sort") String sortBy) {
        return questionService.getAll(pageNum, pageSize, sortBy);
    }

    @PreAuthorize("isFullyAuthenticated()")
    @PostMapping("questions")
    @ResponseStatus(HttpStatus.CREATED)
    public Question createQuestion(@RequestBody QuestionDto questionDto) {
        return questionService.createQuestion(questionDto);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @customAuthenticationManager.isQuestionOwner(authentication, #questionId))")
    @GetMapping("questions/{questionId}/close")
    public Question closeQuestion(@PathVariable("questionId") Integer questionId) {
        return questionService.closeQuestion(questionId);
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "questions/{questionId}/answers", params = {"page", "limit", "sort"})
    public Page<Answer> getAnswersForQuestion(
            @PathVariable("questionId") Integer questionId,
            @RequestParam("page") Integer pageNum,
            @RequestParam("limit") Integer pageSize,
            @RequestParam("sort") String sortBy) {
        return answerService.getByQuestion(questionId, pageNum, pageSize, sortBy);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("questions/{questionId}/answers")
    public List<Answer> getAnswersForQuestion(@PathVariable("questionId") Integer questionId) {
        return answerService.getByQuestion(questionId);
    }

    @PreAuthorize("isFullyAuthenticated()")
    @PostMapping("questions/{questionId}/give-answer")
    public Answer giveAnswer(@PathVariable("questionId") Integer questionId, @RequestBody AnswerDto answerDto) {
        return answerService.createAnswerForQuestion(questionId, answerDto);
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("questions/{questionId}/upvote")
    public Question upVoteQuestion(@PathVariable("questionId") Integer questionId) {
        return questionService.upVote(questionId);
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("questions/{questionId}/downvote")
    public Question downVoteQuestion(@PathVariable("questionId") Integer questionId) {
        return questionService.downVote(questionId);
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("answers/{answerId}/upvote")
    public Answer upVoteAnswer(@PathVariable("answerId") Integer answerId) {
        return answerService.upVote(answerId);
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("answers/{answerId}/downvote")
    public Answer downVoteAnswer(@PathVariable("answerId") Integer answerId) {
        return answerService.downVote(answerId);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @customAuthenticationManager.isQuestionOwner(authentication, #questionId))")
    @GetMapping("questions/{questionId}/set-best-answer/{answerId}")
    public Question setBestAnswer(@PathVariable("questionId") Integer questionId, @PathVariable("answerId") Integer answerId) {
        return questionService.setBestAnswer(questionId, answerId);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @customAuthenticationManager.isQuestionOwner(authentication, #questionId))")
    @GetMapping("questions/{questionId}/unset-best-answer")
    public Question unsetBestAnswer(@PathVariable("questionId") Integer questionId) {
        return questionService.unsetBestAnswer(questionId);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER')) and @customAuthenticationManager.isQuestionOwner(authentication, #questionId))")
    @DeleteMapping("questions/{questionId}")
    public void deleteQuestion(@PathVariable("questionId") Integer questionId) {
        questionService.deleteById(questionId);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @customAuthenticationManager.isAnswerOwner(authentication, #answerId))")
    @DeleteMapping("answers/{answerId}")
    public void deleteAnswer(@PathVariable("answerId") Integer answerId) {
        answerService.deleteById(answerId);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @customAuthenticationManager.isQuestionOwner(authentication, #questionId))")
    @PatchMapping("questions/{questionId}")
    public Question updateQuestion(@PathVariable("questionId") Integer questionId, @RequestBody QuestionDto questionDto) {
        return questionService.updateQuestion(questionId, questionDto);
    }

    @PreAuthorize("hasRole('USER') or (hasRole('USER') and @customAuthenticationManager.isAnswerOwner(authentication, #answerId))")
    @PatchMapping("answers/{answerId}")
    public Answer updateAnswer(@PathVariable("answerId") Integer answerId, @RequestBody AnswerDto answerDto) {
        return answerService.updateAnswerContent(answerId, answerDto);
    }
}
