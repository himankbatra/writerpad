package com.xebia.fs101.writerpad.services;

import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.exceptions.NoUserFoundException;
import com.xebia.fs101.writerpad.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return this.userRepository.save(user);
    }

    public User get(User customUserDetails) {
        Optional<User> foundUser =
                this.userRepository.findById(customUserDetails.getUserid());
        return foundUser.get();
    }

    public User get(String username) {
        User user = userRepository.findByUsernameOrEmail(username, null);
        if (Objects.isNull(user)) {
            throw new NoUserFoundException("User not found.");
        }
        return user;
    }

    public User follow(String username, User user) {

        User followedUser = this.get(username);
        if (!followedUser.getFollowers().contains(user.getUsername())) {
            user.follow();
            followedUser.addFollowers(user.getUsername());
            this.userRepository.save(user);
            User save = this.userRepository.save(followedUser);
            return save;
        }
        return followedUser;
    }

    public User unFollow(String username, User user) {

        User unFollowedUser = this.get(username);
        if (unFollowedUser.getFollowers().contains(user.getUsername())) {
            user.unFollow();
            unFollowedUser.removeFollowers(user.getUsername());
            this.userRepository.save(user);
            User save = this.userRepository.save(unFollowedUser);
            return save;
        }
        return unFollowedUser;
    }


}
