package com.xebia.fs101.writerpad.domain;

public class ReadingTime {

    private int minutes;

    private int seconds;

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public ReadingTime(int minutes, int seconds) {
        this.minutes = minutes;
        this.seconds = seconds;
    }


}
