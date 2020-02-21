package com.xebia.fs101.writerpad.api.rest.representations;

import com.xebia.fs101.writerpad.services.helpers.ReadingTime;

import java.time.Duration;

public class ReadingTimeResponse {

    private String articleId;

    private ReadingTime readingTime;

    public String getArticleId() {
        return articleId;
    }

    public ReadingTime getReadingTime() {
        return readingTime;
    }

    public ReadingTimeResponse(String articleId, Duration readingTime) {
        this.articleId = articleId;
        this.readingTime =
                new ReadingTime((int) readingTime.toMinutes(), (int) readingTime.getSeconds());
    }
}
