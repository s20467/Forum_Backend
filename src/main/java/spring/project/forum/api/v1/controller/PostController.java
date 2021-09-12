package spring.project.forum.api.v1.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

    @GetMapping("questions/{questionId}")
    public Question getQuestionById(@PathVariable("questionId") Integer id){
        return questionService.getById(id);
    }

    @GetMapping("questions")
    public List<Question> getQuestions(){
        return questionService.getAll();
    }

    @GetMapping(value = "questions", params = {"page", "limit", "sort"})
    public Page<Question> getQuestions(
            @RequestParam("page") Integer pageNum,
            @RequestParam("limit") Integer pageSize,
            @RequestParam("sort") String sortBy){
        return questionService.getAll(pageNum, pageSize, sortBy);
    }

    @PostMapping("questions")
    @ResponseStatus(HttpStatus.CREATED)
    public Question createQuestion(@RequestBody QuestionDto questionDto){
        return questionService.createQuestion(questionDto);
    }

    @GetMapping("questions/{questionId}/close")
    public Question closeQuestion(@PathVariable("questionId") Integer questionId){
        return questionService.closeQuestion(questionId);
    }

    @GetMapping(value = "questions/{questionId}/answers", params = {"page", "limit", "sort"})
    public Page<Answer> getAnswersForQuestion(
            @PathVariable("questionId") Integer questionId,
            @RequestParam("page") Integer pageNum,
            @RequestParam("limit") Integer pageSize,
            @RequestParam("sort") String sortBy){
        return answerService.getByQuestion(questionId, pageNum, pageSize, sortBy);
    }

    @GetMapping("questions/{questionId}/answers")
    public List<Answer> getAnswersForQuestion(@PathVariable("questionId") Integer questionId){
        return answerService.getByQuestion(questionId);
    }

    @PostMapping("questions/{questionId}/give-answer")
    public Answer giveAnswer(@PathVariable("questionId") Integer questionId, @RequestBody AnswerDto answerDto){
        return answerService.createAnswerForQuestion(questionId, answerDto);
    }

    @GetMapping("questions/{questionId}/upvote")
    public Question upVoteQuestion(@PathVariable("questionId") Integer questionId){
        //todo implement upVoteQuestion in PostController
        return null;
    }

    @GetMapping("questions/{questionId}/downvote")
    public Question downVoteQuestion(@PathVariable("questionId") Integer questionId){
        //todo implement downVoteQuestion in PostController
        return null;
    }

    @GetMapping("answers/{answerId}/upvote")
    public Answer upVoteAnswer(@PathVariable("answerId") Integer answerId){
        //todo implement upVoteAnswer in PostController
        return null;
    }

    @GetMapping("answers/{answerId}/downvote")
    public Answer downVoteAnswer(@PathVariable("answerId") Integer answerId){
        //todo implement downVoteAnswer in PostController
        return null;
    }

    @GetMapping("questions/{questionId}/set-best-answer/{answerId}")
    public Question setBestAnswer(@PathVariable("questionId") Integer questionId, @PathVariable("answerId") Integer answerId){
        return questionService.setBestAnswer(questionId, answerId);
    }

    @GetMapping("questions/{questionId}/unset-best-answer")
    public Question unsetBestAnswer(@PathVariable("questionId") Integer questionId){
        return questionService.unsetBestAnswer(questionId);
    }

    @DeleteMapping("questions/{questionId}")
    public void deleteQuestion(@PathVariable("questionId") Integer questionId){
        questionService.deleteById(questionId);
    }

    @DeleteMapping("answers/{answerId}")
    public void deleteAnswer(@PathVariable("answerId") Integer answerId){
       answerService.deleteById(answerId);
    }

    @PatchMapping("questions/{questionId}")
    public Question updateQuestion(@PathVariable("questionId") Integer questionId, @RequestBody QuestionDto questionDto){
        return questionService.updateQuestion(questionId, questionDto);
    }

    @PatchMapping("answers/{answerId}")
    public Answer updateAnswer(@PathVariable("answerId") Integer answerId, @RequestBody AnswerDto answerDto){
        return answerService.updateAnswerContent(answerId, answerDto);
    }
}
