package com.bjfu.exam.repository.user;

import com.bjfu.exam.entity.user.User;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    boolean existsByAccount(String account);

    Optional<User> findByAccount(String account);
}
