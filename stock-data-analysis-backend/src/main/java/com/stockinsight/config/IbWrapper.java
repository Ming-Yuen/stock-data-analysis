package com.stockinsight.config;

import com.ib.client.Bar;
import com.ib.client.DefaultEWrapper;
import com.stockinsight.converter.StockConverter;
import com.stockinsight.model.entity.Stock;
import com.stockinsight.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IbWrapper extends DefaultEWrapper {
    private final StockService stockService;
    private final StockConverter stockConverter;

    public void registerRequest(int reqId, String symbol) {
        reqIdToSymbol.put(reqId, symbol);
    }

    @Override
    public void nextValidId(int orderId) {
        log.info("Next valid order ID: {}", orderId);
    }

    @Override
    public void historicalData(int reqId, Bar bar) {
        log.info("Req {}}: {} close={}}", reqId, bar.time(), bar.close());
    }

    @Override
    public void error(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void error(String s) {
        log.error("Error {}", s);
    }

    @Override
    public void error(int i, int i1, String s, String s1) {
        log.error("Error {}, {}, {}, {}", i, i1, s, s1);
    }
}

