package com.xebia.fs101.writerpad.services.helpers;

import com.xebia.fs101.writerpad.exceptions.DuplicateArticleFoundException;
import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class PlagiarismCheckerService {

    private double plagiarismFactor;

    public PlagiarismCheckerService(@Value("${plagiarism.factor}") double plagiarismFactor) {
        this.plagiarismFactor = plagiarismFactor;
    }

    public void checkPlagiarism(String source, Stream<String> targetStream) {
        SimilarityStrategy strategy = new JaroWinklerStrategy();
        StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
        targetStream.forEach(e -> {
            if (service.score(source, e) > plagiarismFactor) {
                throw new DuplicateArticleFoundException("Same article found !!!");
            }
        });
    }

}
