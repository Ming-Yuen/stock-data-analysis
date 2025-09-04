package com.stockinsight.service;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.stockinsight.model.dto.stockInsight.request.SymbolRequest;
import com.stockinsight.model.entity.Stock;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IbService {
    private final EClientSocket client;
    private final StockService stockService;

    public void updateHistoricalStockPrices(SymbolRequest symbolRequest) {
        Contract contract = new Contract();
        contract.secType("STK");
        contract.currency("USD");
        contract.exchange("SMART");

        String endTime = "";        // 空串表示当前时间
        String duration = "10 Y";   // 10 年
        String barSize = "1 day";   // 日线
        int reqId = 1;
        List<String> stocks = new ArrayList<>();
        if("*".equals(symbolRequest.getSymbol())){
            stocks = stockService.findByAll().stream().map(stock -> stock.getSymbol()).collect(Collectors.toList());
        }else{
            if(stockService.existsBySymbol(symbolRequest.getSymbol())){
                stocks.add(symbolRequest.getSymbol());
            }else{
                throw new ValidationException("symbol code not found");
            }
        }

        for(String stock : stocks){
            contract.symbol(stock);
            client.reqHistoricalData(stock., contract, endTime, duration, barSize, "TRADES", 1,   // useRTH: 1=仅盘中
                    1,   // formatDate: 1=yyyyMMdd,时间字段为字符串
                    false,
                    Collections.emptyList()
            );
        }
    }
}
