package com.xebia.fs101.writerpad.utils;

public abstract class StringUtil {
    public static String slug(String title) {
        return title.replace(" ", "-").toLowerCase();
    }

    private StringUtil() {
    }
}
