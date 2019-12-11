package com.xebia.fs101.writerpad.services.helpers;

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

    public boolean isPlagiarism(String source, Stream<String> targetStream) {
        SimilarityStrategy strategy = new JaroWinklerStrategy();
        StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
        return targetStream.anyMatch(e -> service.score(source, e) > plagiarismFactor);
    }

}
