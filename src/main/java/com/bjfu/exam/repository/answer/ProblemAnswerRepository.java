package com.bjfu.exam.repository.answer;

import com.bjfu.exam.entity.answer.ProblemAnswer;
import org.springframework.data.repository.CrudRepository;

public interface ProblemAnswerRepository extends CrudRepository<ProblemAnswer, Long> {
}
