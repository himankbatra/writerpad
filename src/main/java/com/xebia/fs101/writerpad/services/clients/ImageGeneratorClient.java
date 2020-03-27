package com.xebia.fs101.writerpad.services.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@Profile("!test")
public class ImageGeneratorClient implements ImageGenerator {


    private String imageApiUrl;

    private RestTemplate restTemplate;

    public ImageGeneratorClient(RestTemplateBuilder restTemplateBuilder, @Value("${imageApiUrl.url}") String imageApiUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.imageApiUrl = imageApiUrl;
    }

    @Override
    public String generateRandomImage() {
        String result = "";
        try {
            result = Objects.requireNonNull(restTemplate.getForObject(imageApiUrl,
                    ImageResponse.class)).getRegularImageUrl();
        } catch (RestClientException ex) {
            result = "fallback image via placeholder.com";
        }
        return result;
    }

}
