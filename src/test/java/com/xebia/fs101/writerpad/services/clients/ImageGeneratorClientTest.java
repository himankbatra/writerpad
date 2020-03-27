package com.xebia.fs101.writerpad.services.clients;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;


@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = "imageApiUrl.url=http://demo/url")
@RestClientTest(ImageGeneratorClient.class)
class ImageGeneratorClientTest {


    @Autowired
    private ImageGeneratorClient imageGeneratorClient;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockRestServiceServer mockServer;


    @BeforeEach
    void setUp() throws Exception {
        ImageResponse imageResponse = new ImageResponse();
        ImageResponse.ImageUrl imageUrl = new ImageResponse.ImageUrl();
        imageUrl.setRegular("regular");
        imageResponse.setUrls(imageUrl);
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://demo/url")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(imageResponse))
                );
    }


    @Test
    void givenMockingIsDoneByMockRestServiceServer_whenGetIsCalled_thenReturnsMockedObject() {

        String actual = imageGeneratorClient.generateRandomImage();
        mockServer.verify();
        assertThat(actual).isEqualTo("regular");

    }
}



