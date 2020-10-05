package com.bjfu.exam.repository.paper;

import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.user.User;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface PaperRepository extends CrudRepository<Paper, Long> {

    Optional<Paper> findByCode(String code);

    Iterable<Paper> findAllByCreator(User user);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    boolean existsByCode(String code);
}
