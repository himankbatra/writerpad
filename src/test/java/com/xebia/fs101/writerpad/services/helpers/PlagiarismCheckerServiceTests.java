package com.xebia.fs101.writerpad.services.helpers;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PlagiarismCheckerServiceTests {

    private PlagiarismCheckerService plagiarismCheckerService =
            new PlagiarismCheckerService(0.70);

    @Test
    void should_return_true_if_string_is_same() {
        boolean bodyRedundant = plagiarismCheckerService.isPlagiarism("body",
                Stream.of("avcf", "body",
                        "bfgte"));
        assertThat(bodyRedundant).isTrue();
    }

    @Test
    void should_return_false_if_strings_is_not_similar() {
        boolean bodyRedundant =  plagiarismCheckerService.isPlagiarism("abcd",
                Stream.of("iemfeif",
                        "fmefe",
                        "ejfnenfe"));
        assertThat(bodyRedundant).isFalse();
    }


}