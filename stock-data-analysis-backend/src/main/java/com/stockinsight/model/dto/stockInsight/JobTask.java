package com.stockinsight.model.dto.stockInsight;

import com.stockinsight.model.entity.JobConfig;
import lombok.Data;

import java.util.Map;

@Data
public class JobTask {
    private String jobName;
    private JobConfig.TaskGroup taskGroup;
    private Map<String, Object> jobParams;
    private boolean enabled;
}
