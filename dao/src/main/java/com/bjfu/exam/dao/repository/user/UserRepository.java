package com.bjfu.exam.dao.repository.user;

import com.bjfu.exam.dao.entity.user.User;
import com.bjfu.exam.api.enums.UserTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT User FROM User WHERE id=?1")
    Optional<User> findByIdForUpdate(Long id);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT User FROM User WHERE account=?1")
    Optional<User> findByAccountForUpdate(String account);

    Optional<User> findByAccount(String account);

    Page<User> findAllByType(UserTypeEnum type, Pageable pageRequest);
}
