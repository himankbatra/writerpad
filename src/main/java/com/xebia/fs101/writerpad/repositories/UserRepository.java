package com.xebia.fs101.writerpad.repositories;

import com.xebia.fs101.writerpad.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameOrEmail(String username, String email);

}
