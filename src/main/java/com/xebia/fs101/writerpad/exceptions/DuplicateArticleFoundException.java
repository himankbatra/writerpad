package com.xebia.fs101.writerpad.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateArticleFoundException extends RuntimeException {
    public DuplicateArticleFoundException(String message) {
        super(message);
    }
}
