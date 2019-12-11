package com.xebia.fs101.writerpad.utils;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsTests {

    @Test
    void should_be_able_to_slug_the_title() {
        String input = "This is me title";
        String slug = StringUtils.slugify(input);
        assertThat(slug).isEqualTo("this-is-me-title");
    }

    @Test
    void should_collapse_spaces_in_slug() {
        String slug = StringUtils.slugify("How  to    learn Spring Boot");
        assertThat(slug).isEqualTo("how-to-learn-spring-boot");
    }


    @Test
    void should_extract_uuid() {
        UUID slugUuid = StringUtils.toUuid("how-to-learn-spring-boot-03bc41f1-2f62-4aba" +
                "-999e-456d0975300c");
        assertEquals("03bc41f1-2f62-4aba-999e-456d0975300c", slugUuid.toString());
    }

}