//package com.receiver.config;
//
//import com.fasterxml.jackson.databind.JavaType;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//@Slf4j
//@Component
//public class RestTemplateHelper {
//
//    private final RestTemplate restTemplate;
//    private final ObjectMapper objectMapper;
//
//    public RestTemplateHelper(RestTemplate restTemplate, ObjectMapper objectMapper) {
//        this.restTemplate = restTemplate;
//        this.objectMapper = objectMapper;
//    }
//
//    public <T, R> T postForEntity(Class<T> clazz, String url, R body, Object... uriVariables) {
//        try {
//            HttpEntity<R> request = new HttpEntity<>(body);
//            ResponseEntity<String> response =
//                    restTemplate.postForEntity(url, request, String.class, uriVariables);
//
//            JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
//
//            return objectMapper.readValue(response.getBody(), javaType);
//
//        } catch (HttpClientErrorException exception) {
//            log.error("HTTP Error while calling {}: {}", url, exception.getMessage(), exception);
//        } catch (Exception e) {
//            log.error("Error while processing response from {}: {}", url, e.getMessage(), e);
//        }
//        return null; // or throw a custom exception
//    }
//}
//
