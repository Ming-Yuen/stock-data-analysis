package com.stockinsight.model.dto.stockInsight.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

@Data
public class LaunchBatchJobRequest{
    @NotBlank
    private String jobName;

    private Map<String, Object> jobParams;
}
