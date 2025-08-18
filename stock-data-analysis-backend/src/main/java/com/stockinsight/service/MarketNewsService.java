package com.stockinsight.service;

import com.stockinsight.repository.MarketNewsRepository;
import com.stockinsight.model.entity.MarketNews;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketNewsService {
    private final MarketNewsRepository marketNewsRepository;
    public void insert(List<MarketNews> marketNews) {
        marketNewsRepository.saveAll(marketNews);
    }
}
