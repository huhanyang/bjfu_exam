package com.bjfu.exam.repository.paper;

import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PaperRepository extends CrudRepository<Paper, Long> {

    Optional<Paper> findByCode(String code);

    List<Paper> findAllByCreator(User user);

    boolean existsByCode(String code);

    // todo 改写hql
    @Query(value = "select * from exam_paper p where p.id=?1 for update", nativeQuery = true)
    Optional<Paper> findByIdForUpdate(Long id);

    @Query(value = "select count(*) from exam_problem where paper_id = ?1 and polymerization_problem_id is null",
            nativeQuery = true)
    Integer getProblemSize(Long paperId);

    @Query(value = "select count(*) from exam_polymerization_problem where paper_id = ?1",
            nativeQuery = true)
    Integer getPolymerizationProblemSize(Long paperId);
}
