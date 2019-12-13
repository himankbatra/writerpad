package com.xebia.fs101.writerpad.services.clients;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("test")
@Service
public class NoOpImageGeneratorClient implements ImageGenerator {
    @Override
    public String generateRandomImage() {
        return "test image";
    }
}
