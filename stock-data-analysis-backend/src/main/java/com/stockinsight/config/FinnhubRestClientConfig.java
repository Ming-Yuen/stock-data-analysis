package com.stockinsight.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FinnhubRestClientConfig {
    private final FinnhubProperties finnhubProperties;
    @Bean
    public RestClient finnhubRestClient() {
        return RestClient.builder()
                .baseUrl(finnhubProperties.getBaseUrl())
                .requestFactory(httpRequestFactory())
                .requestInterceptor(loggingInterceptor())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    @Bean
    public HttpComponentsClientHttpRequestFactory httpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(finnhubProperties.getTimeout()));
        factory.setReadTimeout(Duration.ofMillis(finnhubProperties.getTimeout()));
        return factory;
    }

    @Bean
    public ClientHttpRequestInterceptor loggingInterceptor() {
        return (request, body, execution) -> {
            ClientHttpResponse response = null;
            try {
                log.info("Request: {}, {}", request.getMethod(), request.getURI());
                response = execution.execute(request, body);
                log.info("Response: {}", response.getStatusCode().value());
                return response;
            } catch (IOException e) {
                log.error("IO Error during request to {}: {}", request.getURI(), e.getMessage(), e);
                throw e;
            } catch (Exception e) {
                log.error("Unexpected error during request to {}: {}", request.getURI(), e.getMessage(), e);
                throw e;
            }
        };
    }

}
