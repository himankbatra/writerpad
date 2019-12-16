package com.xebia.fs101.writerpad.repositories;

import com.xebia.fs101.writerpad.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameOrEmail(String username, String email);

}
