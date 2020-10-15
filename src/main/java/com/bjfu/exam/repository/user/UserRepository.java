package com.bjfu.exam.repository.user;

import com.bjfu.exam.entity.user.User;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    boolean existsByAccount(String account);

    // todo 改写hql
    @Query(value = "select * from exam.exam_user p where p.id=?1 for update", nativeQuery = true)
    Optional<User> findByIdForUpdate(Long id);

    Optional<User> findByAccount(String account);
}
