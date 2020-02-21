package com.xebia.fs101.writerpad.services.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class ReadingTimeService {

    private int averageSpeedInSeconds;

    public ReadingTimeService(
            @Value("${average.speed.in.seconds}") int averageSpeedInSeconds) {
        this.averageSpeedInSeconds = averageSpeedInSeconds;
    }

    public Duration calculateReadingTime(String content) {
        int wordsCount = content.split("\\s").length;
        int readTimeInSeconds = wordsCount / averageSpeedInSeconds;
        return Duration.ofSeconds(readTimeInSeconds);

 /*       int minutes = (readTimeInSeconds / 60) % 60;
        int seconds = readTimeInSeconds % 60;
        return new ReadingTime(minutes, seconds);*/
    }

}
