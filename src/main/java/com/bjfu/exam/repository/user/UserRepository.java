package com.bjfu.exam.repository.user;

import com.bjfu.exam.entity.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
