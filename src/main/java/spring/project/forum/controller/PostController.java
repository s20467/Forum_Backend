package spring.project.forum.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import spring.project.forum.model.Answer;
import spring.project.forum.model.Question;
import spring.project.forum.service.AnswerService;
import spring.project.forum.service.QuestionService;

import java.util.List;

@RestController
@RequestMapping("api/questions")
public class PostController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    public PostController(QuestionService questionService, AnswerService answerService) {
        this.questionService = questionService;
        this.answerService = answerService;
    }

    @GetMapping("{id}")
    public Question getQuestionById(@PathVariable("id") Integer id){
        return questionService.getById(id);
    }

    @GetMapping
    public List<Question> getQuestions(){
        return questionService.getAll();
    }

    @GetMapping(params = {"page", "limit", "sort"})
    public Page<Question> getQuestions(
            @RequestParam("page") Integer pageNum,
            @RequestParam("limit") Integer pageSize,
            @RequestParam("sort") String sortBy){
        return questionService.getAll(pageNum, pageSize, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Question createQuestion(@RequestBody Question question){
        return questionService.createQuestion(question);
    }

    @GetMapping("{id}/close")
    public Question closeQuestion(@PathVariable("id") Integer id){
        return questionService.closeQuestion(id);
    }

    @GetMapping("{id}/answers")
    public Page<Answer> getAnswersForQuestion(
            @PathVariable("id") Integer id,
            @RequestParam("page") Integer pageNum,
            @RequestParam("limit") Integer pageSize,
            @RequestParam("sort") String sortBy){
        return answerService.getByQuestion(id, pageNum, pageSize, sortBy);
    }

    @GetMapping("{id}/give-answer")
    public Answer giveAnswer(@RequestBody Answer answer){
        //todo implement giveAnswer in PostQuestionController
        return null;
    }

    public Question upVoteQuestion(){
        //todo implement upVoteQuestion in PostQuestionController
        return null;
    }

    public Question downVoteQuestion(){
        //todo implement downVoteQuestion in PostQuestionController
        return null;
    }
}
