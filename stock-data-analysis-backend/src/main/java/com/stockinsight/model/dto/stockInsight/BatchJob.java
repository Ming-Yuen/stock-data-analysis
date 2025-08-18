package com.stockinsight.model.dto.stockInsight;

import lombok.Data;

import java.util.Map;

@Data
public class BatchJob {
    private String jobName;
    private Map<String, Object> jobParams;
    private boolean isActive;
}
