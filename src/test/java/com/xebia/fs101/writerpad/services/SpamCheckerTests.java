package com.xebia.fs101.writerpad.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpamCheckerTests {
    @Mock
    private ResourceLoader resourceLoader;
    @InjectMocks
    private SpamChecker spamChecker;

    @BeforeEach
    void setUp() throws IOException {
        when(resourceLoader.getResource(anyString())).thenReturn(new ClassPathResource("spamwords.txt"));
        spamChecker.init();
    }

    @Test
    void should_check_if_string_contains_sapm() throws IOException {
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