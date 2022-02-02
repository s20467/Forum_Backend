package spring.project.forum.api.v1.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.project.forum.api.v1.dto.AnswerDto;
import spring.project.forum.api.v1.dto.AnswerDtoAdmin;
import spring.project.forum.api.v1.dto.QuestionDto;
import spring.project.forum.api.v1.dto.QuestionDtoAdmin;
import spring.project.forum.model.Answer;
import spring.project.forum.model.Question;
import spring.project.forum.service.AnswerService;
import spring.project.forum.service.QuestionService;

import javax.validation.Valid;
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
    public Question getQuestionById(@PathVariable("questionId") Integer id) {
        return questionService.getById(id);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("answers/{answerId}")
    public Answer getAnswerById(@PathVariable("answerId") Integer id) {
        return answerService.getById(id);
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("questions/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public Question createQuestionAdmin(@Valid @RequestBody QuestionDtoAdmin questionDtoAdmin) {
        return questionService.createQuestionAdmin(questionDtoAdmin);
    }

    @PreAuthorize("isFullyAuthenticated()")
    @PostMapping("questions")
    @ResponseStatus(HttpStatus.CREATED)
    public Question createQuestion(@Valid @RequestBody QuestionDto questionDto) {
        return questionService.createQuestion(questionDto);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @customAuthenticationManager.isQuestionOwner(authentication, #questionId))")
    @PostMapping("questions/{questionId}/close")
    public Question closeQuestion(@PathVariable("questionId") Integer questionId) {
        return questionService.closeQuestion(questionId);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @customAuthenticationManager.isQuestionOwner(authentication, #questionId))")
    @PostMapping("questions/{questionId}/open")
    public Question openQuestion(@PathVariable("questionId") Integer questionId) {
        return questionService.openQuestion(questionId);
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

    @PreAuthorize("permitAll()")
    @GetMapping("questions/not-closed")
    public List<Question> getNotClosedQuestions() {
        return questionService.getNotClosed();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("questions/{questionId}/give-answer/admin")
    public Answer giveAnswerAdmin(@PathVariable("questionId") Integer questionId, @Valid @RequestBody AnswerDtoAdmin answerDtoAdmin) {
        return answerService.createAnswerForQuestionAdmin(questionId, answerDtoAdmin);
    }

    @PreAuthorize("isFullyAuthenticated()")
    @PostMapping("questions/{questionId}/give-answer")
    public Answer giveAnswer(@PathVariable("questionId") Integer questionId, @Valid @RequestBody AnswerDto answerDto) {
        return answerService.createAnswerForQuestion(questionId, answerDto);
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("questions/{questionId}/upvote")
    public Question upVoteQuestion(@PathVariable("questionId") Integer questionId) {
        return questionService.upVote(questionId);
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("questions/{questionId}/unupvote")
    public Question unUpVoteQuestion(@PathVariable("questionId") Integer questionId) {
        return questionService.unUpVote(questionId);
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("questions/{questionId}/downvote")
    public Question downVoteQuestion(@PathVariable("questionId") Integer questionId) {
        return questionService.downVote(questionId);
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("questions/{questionId}/undownvote")
    public Question unDownVoteQuestion(@PathVariable("questionId") Integer questionId) {
        return questionService.unDownVote(questionId);
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

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("answers/{answerId}/unupvote")
    public Answer unUpVoteAnswer(@PathVariable("answerId") Integer answerId) {
        return answerService.unUpVote(answerId);
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("answers/{answerId}/undownvote")
    public Answer unDownVoteAnswer(@PathVariable("answerId") Integer answerId) {
        return answerService.unDownVote(answerId);
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

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @customAuthenticationManager.isQuestionOwner(authentication, #questionId))")
    @DeleteMapping("questions/{questionId}")
    public void deleteQuestion(@PathVariable("questionId") Integer questionId) {
        questionService.deleteById(questionId);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @customAuthenticationManager.isAnswerOwner(authentication, #answerId))")
    @DeleteMapping("answers/{answerId}")
    public void deleteAnswer(@PathVariable("answerId") Integer answerId) {
        answerService.deleteById(answerId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("questions/{questionId}/admin")
    public Question updateQuestionAdmin(@PathVariable("questionId") Integer questionId, @Valid @RequestBody QuestionDtoAdmin questionDtoAdmin) {
        return questionService.updateQuestionAdmin(questionId, questionDtoAdmin);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @customAuthenticationManager.isQuestionOwner(authentication, #questionId))")
    @PatchMapping("questions/{questionId}")
    public Question updateQuestion(@PathVariable("questionId") Integer questionId, @Valid @RequestBody QuestionDto questionDto) {
        return questionService.updateQuestion(questionId, questionDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("answers/{answerId}/admin")
    public Answer updateAnswerAdmin(@PathVariable("answerId") Integer answerId, @Valid @RequestBody AnswerDtoAdmin answerDtoAdmin) {
        return answerService.updateAnswerAdmin(answerId, answerDtoAdmin);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @customAuthenticationManager.isAnswerOwner(authentication, #answerId))")
    @PatchMapping("answers/{answerId}")
    public Answer updateAnswer(@PathVariable("answerId") Integer answerId, @Valid @RequestBody AnswerDto answerDto) {
        return answerService.updateAnswer(answerId, answerDto);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("questions/get-by-author/{username}")
    public List<Question> getQuestionsByAuthor(@PathVariable("username") String username) {
        return this.questionService.getByAuthor(username);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("questions/answered-by/{username}")
    public List<Question> getQuestionsAnsweredByUser(@PathVariable("username") String username) {
        return this.questionService.getQuestionsAnsweredByUser(username);
    }
}
