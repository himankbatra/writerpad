package com.xebia.fs101.writerpad.services.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class ImageGeneratorService {

    private String featuredImageUrl;


    public ImageGeneratorService(@Value("${featuredImage.url}") String featuredImageUrl) {
        this.featuredImageUrl = featuredImageUrl;
    }

    public String generateRandomImage() {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(featuredImageUrl,
                String.class);
        try {
            JsonNode jsonNode =
                    new ObjectMapper().readTree(Objects.requireNonNull(result));
            return jsonNode.get("urls").get("regular").toString();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("error generating featured image");
        }
    }

}
