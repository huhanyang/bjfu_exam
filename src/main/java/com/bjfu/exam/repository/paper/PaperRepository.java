package com.bjfu.exam.repository.paper;

import com.bjfu.exam.entity.paper.Paper;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PaperRepository extends CrudRepository<Paper, Long> {
    Optional<Paper> findByCode(String code);
}
