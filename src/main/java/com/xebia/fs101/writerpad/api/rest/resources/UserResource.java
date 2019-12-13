package com.xebia.fs101.writerpad.api.rest.resources;

import com.xebia.fs101.writerpad.api.rest.representations.UserRequest;
import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.services.UserService;
import com.xebia.fs101.writerpad.services.security.AdminOnly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/users")
public class UserResource {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @AdminOnly
    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody UserRequest userRequest) {
        User savedUser = this.userService.save(userRequest.toUser(passwordEncoder));
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "username should be unique")
    @ExceptionHandler(DataIntegrityViolationException.class)
    void error(Exception ex) {
        // log error
    }

}
