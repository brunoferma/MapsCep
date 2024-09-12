package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
    @Value("google.api.key")
    private String googleApiKey;

    public String getGoogleApiKey() {
        return googleApiKey;
    }
}