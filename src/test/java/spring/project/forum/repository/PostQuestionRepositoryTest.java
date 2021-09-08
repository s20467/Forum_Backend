package spring.project.forum.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import spring.project.forum.model.PostQuestion;


import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class PostQuestionRepositoryTest {

    private final PostQuestionRepository postQuestionRepository;

    @Autowired
    PostQuestionRepositoryTest(PostQuestionRepository postQuestionRepository) {
        this.postQuestionRepository = postQuestionRepository;
    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(1, 2);
        Page<PostQuestion> postQuestions = postQuestionRepository.findAll(pageable);
        List<PostQuestion> pql = new ArrayList<>();
        postQuestions.get().forEach(pql::add);
        System.out.println(pql.size());
    }
}