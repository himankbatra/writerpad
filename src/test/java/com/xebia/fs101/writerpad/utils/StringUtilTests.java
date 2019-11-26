package com.xebia.fs101.writerpad.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringUtilTests {

    @Test
    void should_be_able_to_slug_the_title() {
        String input = "This is me title";
        String slug = StringUtil.slug(input);
        assertThat(slug).isEqualTo("this-is-me-title");
    }

}