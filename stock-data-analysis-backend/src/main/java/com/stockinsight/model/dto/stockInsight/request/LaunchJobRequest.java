package com.stockinsight.model.dto.stockInsight.request;

import com.stockinsight.model.entity.JobConfig;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class LaunchJobRequest {
    @NotBlank
    private String jobName;

    private Map<String, Object> jobParams;
    @NotNull
    private JobConfig.TaskGroup taskGroup;
}
