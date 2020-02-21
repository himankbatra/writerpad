package com.xebia.fs101.writerpad.services;

import com.xebia.fs101.writerpad.services.helpers.ReadingTime;
import com.xebia.fs101.writerpad.services.helpers.ReadingTimeService;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class ReadingTimeServiceTests {

    @Test
    void should_get_reading_time_when_i_provide_valid_data() {
        ReadingTimeService readingTimeService = new ReadingTimeService(3);
        Duration readingTime = readingTimeService.calculateReadingTime("a b c");
        assertThat(readingTime).isEqualTo(Duration.ofSeconds(1));

    }

    @Test
    void should_get_reading_time_with_minutes_when_i_provide_valid_data() {
        ReadingTimeService readingTimeService = new ReadingTimeService(1);
        String content =
                IntStream.rangeClosed(1, 63).mapToObj(i -> "" + i).collect(Collectors.joining(" "));
        Duration readingTime = readingTimeService.calculateReadingTime(content);
        assertThat(readingTime).isEqualTo(Duration.ofMinutes(1).plusSeconds(3));

    }
}