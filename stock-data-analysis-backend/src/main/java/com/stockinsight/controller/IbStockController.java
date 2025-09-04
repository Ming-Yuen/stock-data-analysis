package com.stockinsight.controller;

import com.stockinsight.model.dto.stockInsight.request.SymbolRequest;
import com.stockinsight.service.IbService;
import com.ykm.common.common_lib.model.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller("ib")
@RequiredArgsConstructor
public class IbStockController {
    private final IbService ibService;


    public void updateHistoricalStockPrices(SymbolRequest symbolRequest){
        ibService.updateHistoricalStockPrices(symbolRequest);
    }
}
