package spring.project.forum.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.project.forum.model.PostQuestion;
import spring.project.forum.model.security.User;
import spring.project.forum.repository.PostQuestionRepository;
import spring.project.forum.repository.security.UserRepository;
import spring.project.forum.service.PostQuestionService;

@RestController
@RequestMapping("api/questions")
public class PostQuestionController {

    private final UserRepository userRepository;
    private final PostQuestionRepository questionRepository;
    private final PostQuestionService postQuestionService;

    public PostQuestionController(UserRepository userRepository, PostQuestionRepository questionRepository, PostQuestionService postQuestionService) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.postQuestionService = postQuestionService;
    }

    @GetMapping("user")
    public User getUserTest(){
        User user = userRepository.findByUsername("user1").get();
        return user;
    }

    @GetMapping("{id}")
    public PostQuestion getQuestionById(@PathVariable("id") Integer id){
        return postQuestionService.getById(id);
    }


}
