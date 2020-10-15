package com.bjfu.exam.repository.answer;

import com.bjfu.exam.entity.answer.PaperAnswer;
import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PaperAnswerRepository extends CrudRepository<PaperAnswer, Long> {
    Optional<PaperAnswer> findByUserAndPaper(User user, Paper paper);
    List<PaperAnswer> findAllByUser(User user);
    // todo 改写hql
    @Query(value = "select * from exam.exam_paper_answer p where p.id=?1 for update", nativeQuery = true)
    Optional<PaperAnswer> findByIdForUpdate(Long id);
}
