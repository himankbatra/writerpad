package com.xebia.fs101.writerpad.utils;

public abstract class StringUtils {
    public static String slugify(String input) {
        if (input == null) {
            throw new IllegalArgumentException("input can't be null");
        }
        input = input.trim().toLowerCase()
                .replaceAll("\\s\\s+", " ");
        return input.replaceAll("\\s", "-");
    }

    private StringUtils() {
    }
}
