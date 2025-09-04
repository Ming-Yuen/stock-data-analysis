package com.stockinsight.scheduling;

import com.stockinsight.converter.StockConverter;
import com.stockinsight.model.dto.finnhub.response.Symbol;
import com.stockinsight.model.entity.Exchange;
import com.stockinsight.model.entity.Stock;
import com.stockinsight.service.ExchangeService;
import com.stockinsight.service.FinnhubService;
import com.stockinsight.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class StockDailyTask implements Job {
    private final FinnhubService finnhubService;
    private final ExchangeService exchangeService;
    private final StockService stockService;
    private final StockConverter stockConverter;

    @Override
    public void execute(JobExecutionContext context) {
        for(Exchange exchange : exchangeService.getExchange()){
            LocalDate previousTradingDay = exchangeService.getPreviousTradingDay(LocalDate.now());
            List<Symbol> symbols = finnhubService.getStockSymbols(exchange.getExchangeCode());
            ArrayList<Stock> stocks = stockConverter.toStocks(symbols);
            stockService.stockUpdate(stocks);
        }
    }
}
