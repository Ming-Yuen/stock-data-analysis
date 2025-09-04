package com.stockinsight.model.dto.stockInsight.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SymbolRequest {
    @NotBlank
    private String symbol;
}
