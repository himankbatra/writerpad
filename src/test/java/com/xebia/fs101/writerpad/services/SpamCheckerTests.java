package com.xebia.fs101.writerpad.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SpamCheckerTests {

    private SpamChecker spamChecker;

    @BeforeEach
    void setUp() throws IOException {
        spamChecker = new SpamChecker();
        spamChecker.spamWords= new HashSet<>(Arrays.asList("semen","spam"));
    }

    @Test
    void should_check_if_string_contains_spam() throws IOException {
        String input = "Hello";
        boolean spam = spamChecker.isSpam(input);
        assertThat(spam).isFalse();
    }

    @Test
    void should_return_true_if_spam() throws IOException {
        String input = "semen";
        boolean spam = spamChecker.isSpam(input);
        assertThat(spam).isTrue();
    }

}