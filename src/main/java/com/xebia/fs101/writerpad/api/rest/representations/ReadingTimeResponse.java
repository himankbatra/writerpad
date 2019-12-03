package com.xebia.fs101.writerpad.api.rest.representations;

import com.xebia.fs101.writerpad.domain.ReadingTime;

public class ReadingTimeResponse {

    private String articleId;

    private ReadingTime readingTime;

    public String getArticleId() {
        return articleId;
    }

    public ReadingTime getReadingTime() {
        return readingTime;
    }

    public ReadingTimeResponse(String articleId, ReadingTime readingTime) {
        this.articleId = articleId;
        this.readingTime = readingTime;
    }
}
