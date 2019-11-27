package com.xebia.fs101.writerpad.services;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class SpamChecker {

    private Set<String> spamWords;


    public SpamChecker() {
        this.spamWords = new HashSet<>();
        this.spamWords.addAll(Arrays.asList("acne", "adult", ""));
    }

    public boolean isSpam(String content) {
        Set<String> words = new HashSet<>(Arrays.asList(content.split("\\s")));
        if (!Collections.disjoint(spamWords, words)) {
            return true;
        }
        return false;
    }
}
