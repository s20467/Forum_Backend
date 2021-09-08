package spring.project.forum.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import spring.project.forum.exception.ResourceNotFoundException;
import spring.project.forum.model.PostQuestion;
import spring.project.forum.repository.PostQuestionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PostQuestionServiceImpl implements PostQuestionService{

    private final PostQuestionRepository postQuestionRepository;

    public PostQuestionServiceImpl(PostQuestionRepository postQuestionRepository) {
        this.postQuestionRepository = postQuestionRepository;
    }

    public PostQuestion getPagedSortedBy(String sortedBy){
        //todo implement get postQuestion getById in PostQuestionService
        return null;
    }


    public PostQuestion getById(Integer id){
        Optional<PostQuestion> postQuestionOptional = postQuestionRepository.findById(id);
        if(postQuestionOptional.isEmpty())
            throw new ResourceNotFoundException("Question with id " + id + " not found");
        return postQuestionOptional.get();
    }

    public PostQuestion getPagedByUsername(String username){
        //todo implement get postQuestion getById in PostQuestionService
        return null;
    }
}
