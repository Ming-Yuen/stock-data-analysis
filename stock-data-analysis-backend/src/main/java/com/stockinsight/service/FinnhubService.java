package com.stockinsight.service;

import com.google.common.util.concurrent.RateLimiter;
import com.stockinsight.config.FinnhubProperties;
import com.stockinsight.model.dto.finnhub.response.MarketNews;
import com.stockinsight.model.dto.finnhub.response.Quote;
import com.stockinsight.model.dto.finnhub.response.Recommendation;
import com.stockinsight.model.dto.finnhub.response.Symbol;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FinnhubService {
    private final RestClient restClient;
    private final FinnhubProperties finnhubProperties;

    private final RateLimiter rateLimiter = RateLimiter.create(58.0 / 60.0);

    @Retryable(
            value = {ResourceAccessException.class, SocketTimeoutException.class, HttpClientErrorException.class, HttpClientErrorException.class, HttpClientErrorException.Forbidden.class},
            maxAttempts = 1000,
            backoff = @Backoff(delay = 1000)
    )
    public <T> T get(String path, Map<String, Object> queryParams, ParameterizedTypeReference<T> responseType) {
        rateLimiter.acquire();
        return restClient
                .get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder.path(path);
                    if (queryParams != null) {
                        queryParams.forEach(builder::queryParam);
                    }
                    builder.queryParam("token", finnhubProperties.getApiKey());

                    return builder.build();
                })
                .retrieve()
                .body(responseType);
    }

    public List<Symbol> getStockSymbols(String exchange) {
        Map<String, Object> params = Map.of("exchange", exchange);
        return get("/stock/symbol", params, new ParameterizedTypeReference<List<Symbol>>() {});
    }

    public List<MarketNews> getMarketNews(String category) {
        Map<String, Object> params = Map.of("category", category);
        return get("/news", params, new ParameterizedTypeReference<List<MarketNews>>() {});
    }

    public Quote getStockQuote(String symbol) {
        Map<String, Object> params = Map.of("symbol", symbol);
        return get("/quote", params, new ParameterizedTypeReference<Quote>() {});
    }

    public Recommendation getRecommendation(String symbol) {
        Map<String, Object> params = Map.of("symbol", symbol);
        return get("/recommendation", params, new ParameterizedTypeReference<Recommendation>() {});
    }
}