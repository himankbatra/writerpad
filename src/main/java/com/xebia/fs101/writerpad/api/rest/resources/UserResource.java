package com.xebia.fs101.writerpad.api.rest.resources;

import com.xebia.fs101.writerpad.api.rest.representations.UserRequest;
import com.xebia.fs101.writerpad.api.rest.representations.UserResponse;
import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.services.UserService;
import com.xebia.fs101.writerpad.services.security.AdminOnly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/api")
public class UserResource {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @AdminOnly
    @PostMapping("/users")
    public ResponseEntity<User> create(@Valid @RequestBody UserRequest userRequest) {
        User savedUser = this.userService.save(userRequest.toUser(passwordEncoder));
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "username should be unique")
    @ExceptionHandler(DataIntegrityViolationException.class)
    void error(Exception ex) {
        // log error
    }


    @GetMapping("/profiles/{username}")
    public ResponseEntity<UserResponse> get(@PathVariable(name = "username") String username,
                                            @AuthenticationPrincipal User customUserDetails) {
        User user = userService.get(username);
        if (!Objects.isNull(customUserDetails)) {
            if (user.getFollowers()
                    .contains(this.userService.get(customUserDetails).getUsername())) {
                user.setFollowing(true);
            }
        }
        return new ResponseEntity<>(UserResponse.from(user), OK);
    }


    @PostMapping(path = "/profiles/{username}/follow")
    public ResponseEntity<UserResponse> follow(@AuthenticationPrincipal User customUserDetails
            , @PathVariable(value = "username") String username) {

        User user = this.userService.get(customUserDetails);

        if (Objects.equals(user.getUsername(), username)) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
        User userToBeFollowed = userService.follow(username, user);
        userToBeFollowed.setFollowing(true);
        return new ResponseEntity<>(UserResponse.from(userToBeFollowed), OK);
    }

    @DeleteMapping(path = "/profiles/{username}/unfollow")
    public ResponseEntity<UserResponse> unFollow(@AuthenticationPrincipal User customUserDetails
            , @PathVariable(value = "username") String username) {
        User user = this.userService.get(customUserDetails);
        if (Objects.equals(user.getUsername(), username)) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }
        User userToBeUnFollowed = userService.unFollow(username, user);
        return new ResponseEntity<>(UserResponse.from(userToBeUnFollowed), OK);
    }


}
