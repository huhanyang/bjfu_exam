package com.bjfu.exam.repository.paper;

import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.user.User;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface PaperRepository extends CrudRepository<Paper, Long> {

    Optional<Paper> findByCode(String code);

    Iterable<Paper> findAllByCreator(User user);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    boolean existsByCode(String code);


    @Query(value = "select count(*) from exam_problem where paper_id = ?1 and polymerization_problem_id is null",
            nativeQuery = true)
    Integer getProblemSize(Long paperId);

    @Query(value = "select count(*) from exam_polymerization_problem where paper_id = ?1",
            nativeQuery = true)
    Integer getPolymerizationProblemSize(Long paperId);
}
