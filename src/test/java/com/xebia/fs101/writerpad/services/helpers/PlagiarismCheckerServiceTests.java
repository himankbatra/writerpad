package com.xebia.fs101.writerpad.services.helpers;

import com.xebia.fs101.writerpad.exceptions.DuplicateArticleFoundException;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlagiarismCheckerServiceTests {

    private PlagiarismCheckerService plagiarismCheckerService =
            new PlagiarismCheckerService(0.70);

    @Test
    void should_throw_exception_if_string_is_plagiarism_in_list_of_string() {


        DuplicateArticleFoundException thrown =
                assertThrows(DuplicateArticleFoundException.class,
                        () -> plagiarismCheckerService.checkPlagiarism("body",
                                Stream.of("avcf", "body",
                                        "bfgte")),
                        "Expected doThing() to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("Same article found !!!"));

    }

    @Test
    void should_not_throw_exception_if_string_is_not_plagiarism_in_list_of_string() {
        assertDoesNotThrow(
                () -> plagiarismCheckerService.checkPlagiarism("body",
                        Stream.of("avcf",
                                "bfgte")),
                "Expected doThing() to throw, but it didn't");
    }


}