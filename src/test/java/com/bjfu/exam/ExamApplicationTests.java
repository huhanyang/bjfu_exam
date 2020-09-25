package com.bjfu.exam;

import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.repository.paper.PaperRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class ExamApplicationTests {

    @Autowired
    private PaperRepository paperRepository;

    @Test
    void contextLoads() {
        Optional<Paper> paper1 = paperRepository.findById((long) 1);
        System.out.println(paper1.get());
    }

}
