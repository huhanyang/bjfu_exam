package com.bjfu.exam.repository.paper;

import com.bjfu.exam.entity.paper.Problem;
import org.springframework.data.repository.CrudRepository;

public interface ProblemRepository extends CrudRepository<Problem, Long> {
}
