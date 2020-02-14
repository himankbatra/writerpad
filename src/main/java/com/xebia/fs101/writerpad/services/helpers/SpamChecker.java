package com.xebia.fs101.writerpad.services.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SpamChecker {


    Set<String> spamWords;

    @Value("${file.spamwords}")
    private File spamWordFile;

    @PostConstruct
    public void init() throws IOException {
        List<String> lines = Files.readAllLines(spamWordFile.toPath());
        this.spamWords = new HashSet<>(lines);
    }

    public Set<String> getSpamWords() {
        return spamWords;
    }

    public boolean isSpam(String content) {

        Set<String> words = new HashSet<>(Arrays.asList(content.toLowerCase().split("\\s")));
        if (!Collections.disjoint(spamWords, words)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpamChecker that = (SpamChecker) o;

        if (spamWords != null
                ? !spamWords.equals(that.spamWords) : that.spamWords != null) return false;
        return spamWordFile != null
                ? spamWordFile.equals(that.spamWordFile) : that.spamWordFile == null;
    }

    @Override
    public int hashCode() {
        int result = spamWords != null ? spamWords.hashCode() : 0;
        result = 31 * result + (spamWordFile != null ? spamWordFile.hashCode() : 0);
        return result;
    }
}
