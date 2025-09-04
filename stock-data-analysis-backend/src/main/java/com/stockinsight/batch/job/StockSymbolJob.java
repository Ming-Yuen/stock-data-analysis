package com.stockinsight.batch.job;

import com.stockinsight.batch.config.BatchJobFileParameter;
import com.stockinsight.converter.StockConverter;
import com.stockinsight.model.dto.finnhub.response.Symbol;
import com.stockinsight.model.entity.Exchange;
import com.stockinsight.model.entity.Stock;
import com.stockinsight.service.ExchangeService;
import com.stockinsight.service.FinnhubService;
import com.stockinsight.service.StockService;
import com.ykm.common.common_lib.batch.factory.CsvMultiResourceReaderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StockSymbolJob {
    private final FinnhubService finnhubService;
    private final ExchangeService exchangeService;
    private final StockService stockService;
    private final StockConverter stockConverter;

    @Bean
    public Job downloadExchangeData(JobRepository jobRepository, Step downloadExchangeStep) {
        return new JobBuilder("downloadExchangeData", jobRepository)
                .start(downloadExchangeStep)
                .build();
    }

    @Bean
    public Step downloadExchangeStep(JobRepository jobRepository, PlatformTransactionManager txManager,
                      ItemReader<Exchange> reader,
                      ItemWriter<Exchange> writer) {
        return new StepBuilder("downloadExchangeStep", jobRepository)
                .<Exchange, Exchange>chunk(100, txManager)
                .reader(reader)
                .writer(writer)
                .faultTolerant()
                .retryLimit(3000)
                .retry(Exception.class)
                .build();
    }

    @Bean
    @StepScope
    public MultiResourceItemReader<Exchange> batchJobFileReader(BatchJobFileParameter batchJobFileParameter) {
        MultiResourceItemReader<Exchange> reader = CsvMultiResourceReaderFactory.create(
                batchJobFileParameter.getBaseDir(),
                batchJobFileParameter.getFilePattern(),
                batchJobFileParameter.getColumns(),
                batchJobFileParameter.getFieldSetMapperClass()
        );
        return reader;
    }

    @Bean
    public ItemWriter<Exchange> exchangeItemWriter() {
        return chunk -> {
            List<Exchange> exchanges = exchangeService.getExchange();
            for(Exchange exchange : exchanges) {
                List<Symbol> symbols = finnhubService.getStockSymbols(exchange.getExchangeCode());
                ArrayList<Stock> stocks = stockConverter.toStocks(symbols);
                stockService.stockUpdate(stocks);
            }
        };
    }
}
