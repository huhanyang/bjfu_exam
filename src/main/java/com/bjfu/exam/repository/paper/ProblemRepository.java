package com.bjfu.exam.repository.paper;

import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.paper.PolymerizationProblem;
import com.bjfu.exam.entity.paper.Problem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProblemRepository extends CrudRepository<Problem, Long> {
    void deleteAllByPaper(Paper paper);
    void deleteAllByPolymerizationProblem(PolymerizationProblem polymerizationProblem);
    Optional<Problem> findByPaperAndPolymerizationProblemAndSort(Paper paper, PolymerizationProblem polymerizationProblem, Integer sort);

    // todo 改写hql
    @Query(value = "select * from exam.exam_polymerization_problem p where p.id=?1 for update", nativeQuery = true)
    Optional<Problem> findByIdForUpdate(Long id);
}
