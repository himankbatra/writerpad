package com.xebia.fs101.writerpad.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;


class SpamCheckerTests {

    private SpamChecker spamChecker;

    @BeforeEach
    void setUp() {
        spamChecker = new SpamChecker();
        spamChecker.spamWords = new HashSet<>(Arrays.asList("semen", "spam"));
    }

    @Test
    void should_check_if_string_contains_spam() {
        String input = "Hello";
        boolean spam = spamChecker.isSpam(input);
        assertThat(spam).isFalse();
    }

    @Test
    void should_return_true_if_spam() {
        String input = "semen";
        boolean spam = spamChecker.isSpam(input);
        assertThat(spam).isTrue();
    }

}