package com.bjfu.exam.repository.paper;

import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.paper.PolymerizationProblem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PolymerizationProblemRepository extends CrudRepository<PolymerizationProblem, Long> {
    void deleteAllByPaper(Paper paper);
    Optional<PolymerizationProblem> findByPaperAndSort(Paper paper, Integer sort);

    // todo 改写hql
    @Query(value = "select * from exam.exam_polymerization_problem p where p.id=?1 for update", nativeQuery = true)
    Optional<PolymerizationProblem> findByIdForUpdate(Long id);
}
