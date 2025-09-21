package com.encryption.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RestTemplateHelper {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RestTemplateHelper(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public <T, R> T postForEntity(Class<T> clazz, String url, R body, Object... uriVariables) {
        try {
            // 1. Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 2. Wrap body and headers in HttpEntity
            HttpEntity<R> request = new HttpEntity<>(body, headers);

            // 3. Make POST request
            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, request, String.class, uriVariables);

            // 4. Convert JSON string response to object of type T
            return objectMapper.readValue(response.getBody(), clazz);

        } catch (HttpClientErrorException exception) {
            log.error("HTTP Error while calling {}: {}", url, exception.getMessage(), exception);
        } catch (Exception e) {
            log.error("Error while processing response from {}: {}", url, e.getMessage(), e);
        }
        return null; // or throw a custom exception
    }

}

