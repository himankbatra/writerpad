package com.xebia.fs101.writerpad.api.rest.representations;

public class TagResponse {

    private String tag;
    private long occurrence;

    public String getTag() {
        return tag;
    }

    public long getOccurrence() {
        return occurrence;
    }

    public TagResponse(String tag, long occurrence) {
        this.tag = tag;
        this.occurrence = occurrence;
    }
}
