package com.xebia.fs101.writerpad.api.rest.representations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.xebia.fs101.writerpad.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@JsonDeserialize(builder = UserRequest.Builder.class)
public class UserRequest {

    @NotBlank(message = "Please Provide username")
    private final String username;

    @NotBlank(message = "Please Provide email")
    @Email(message = "Please Provide valid email")
    private final String email;


    @NotBlank(message = "Please Provide password")
    private final String password;


    private UserRequest(Builder builder) {
        username = builder.username;
        email = builder.email;
        password = builder.password;
    }


    public User toUser(PasswordEncoder passwordEncoder) {
        return new User.Builder().withUsername(this.username)
                .withEmail(this.email)
                .withPassword(passwordEncoder.encode(this.password))
                .build();
    }

    @JsonPOJOBuilder
    public static final class Builder {

        private @NotBlank(message = "Please Provide username") String username;
        private @NotBlank(message = "Please Provide email") @Email(message = "Please "
                + "Provide valid email") String email;
        private @NotBlank(message = "Please Provide password") String password;

        public Builder() {
        }

        public Builder withUsername(@NotBlank(message = "Please Provide username") String val) {
            username = val;
            return this;
        }

        public Builder withEmail(@NotBlank(message = "Please Provide email")
                                 @Email(message = "Please Provide valid email") String val) {
            email = val;
            return this;
        }

        public Builder withPassword(@NotBlank(message = "Please Provide password") String val) {
            password = val;
            return this;
        }

        public UserRequest build() {
            return new UserRequest(this);
        }

    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}