package com.bjfu.exam.repository.paper;

import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.paper.Problem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends CrudRepository<Problem, Long> {
    void deleteAllByPaper(Paper paper);
    List<Problem> findByPaperAndSort(Paper paper, Integer sort);

    // todo 改写hql
    @Query(value = "select * from exam.exam_problem p where p.id=?1 for update", nativeQuery = true)
    Optional<Problem> findByIdForUpdate(Long id);
}
