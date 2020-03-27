package com.xebia.fs101.writerpad.services.clients;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageGeneratorClientMockTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ImageGeneratorClient imageGeneratorClient = new ImageGeneratorClient(new RestTemplateBuilder(), "http://demo/url");


    @Test
    public void givenMockingIsDoneByMockito_whenGetIsCalled_shouldReturnMockedObject() {

        ImageResponse imageResponse = new ImageResponse();
        ImageResponse.ImageUrl imageUrl = new ImageResponse.ImageUrl();
        imageUrl.setRegular("regular");
        imageResponse.setUrls(imageUrl);
        when(restTemplate.getForObject(
                "http://demo/url", ImageResponse.class))
                .thenReturn(imageResponse);


        String actual = imageGeneratorClient.generateRandomImage();

        assertThat(actual).isEqualTo("regular");

    }

}

