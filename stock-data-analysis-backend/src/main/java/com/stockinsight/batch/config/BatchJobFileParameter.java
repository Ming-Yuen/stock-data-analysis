package com.stockinsight.batch.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@StepScope
public class BatchJobFileParameter {
    @NotBlank
    @Value("#{jobParameters['baseDir']}")
    private String baseDir;
    @NotBlank
    @Value("#{jobParameters['filePattern']}")
    private String filePattern;
    @NotBlank
    @Value("#{jobParameters['columns'].split(',')}")
    private String[] columns;
    @NotBlank
    @Value("#{(Class).forName(jobParameters['fieldSetMapperClass'])}")
    private Class fieldSetMapperClass;
}
