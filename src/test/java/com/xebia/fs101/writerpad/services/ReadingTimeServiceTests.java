package com.xebia.fs101.writerpad.services;

import com.xebia.fs101.writerpad.domain.ReadingTime;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class ReadingTimeServiceTests {

    @Test
    void should_get_reading_time_when_i_provide_valid_data() {
        ReadingTimeService readingTimeService = new ReadingTimeService(3);
        ReadingTime readingTime = readingTimeService.calculateReadingTime("a b c");
        assertThat(readingTime).isEqualToComparingFieldByField(new ReadingTime(0, 1));

    }

    @Test
    void should_get_reading_time_with_minutes_when_i_provide_valid_data() {
        ReadingTimeService readingTimeService = new ReadingTimeService(1);
        String content =
                IntStream.rangeClosed(1, 63).mapToObj(i -> "" + i).collect(Collectors.joining(" "));
        ReadingTime readingTime = readingTimeService.calculateReadingTime(content);
        assertThat(readingTime).isEqualToComparingFieldByField(new ReadingTime(1, 3));

    }
}