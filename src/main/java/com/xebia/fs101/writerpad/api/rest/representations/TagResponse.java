package com.xebia.fs101.writerpad.api.rest.representations;

public class TagResponse {

    private String tag;
    private long occurance;

    public String getTag() {
        return tag;
    }

    public long getOccurance() {
        return occurance;
    }

    public TagResponse(String tag, long occurance) {
        this.tag = tag;
        this.occurance = occurance;
    }
}
