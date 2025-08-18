package com.stockinsight.service;

import com.stockinsight.converter.BatchConverter;
import com.stockinsight.repository.BatchJonConfigRepository;
import com.stockinsight.model.dto.stockInsight.BatchJob;
import com.stockinsight.model.entity.BatchJonConfig;
import com.stockinsight.model.dto.stockInsight.request.EnquiryBatchJobRequest;
import com.stockinsight.model.dto.stockInsight.request.LaunchBatchJobRequest;
import com.stockinsight.model.dto.stockInsight.response.EnquiryBatchJobResponse;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BatchService {
    private final JobLauncher launcher;
    private final JobRegistry registry;
    private final BatchJonConfigRepository batchJonConfigRepository;
    private final BatchConverter batchConverter;

    public EnquiryBatchJobResponse enquiryBatchJob(EnquiryBatchJobRequest enquiryBatchJobRequest) {
        List<BatchJonConfig> batchJonConfigList = batchJonConfigRepository.findAll();

        EnquiryBatchJobResponse response = new EnquiryBatchJobResponse();
        List<BatchJob> batchJobList = batchJonConfigList.stream()
                .filter(job -> StringUtils.isBlank(enquiryBatchJobRequest.getJobName()) || job.getName().equals(enquiryBatchJobRequest.getJobName()))
                .map(job->batchConverter.jobToBatchJobListResponse(job))
                .collect(Collectors.toList());
        response.setBatchJobList(batchJobList);

        return response;
    }

    public void launchBatchJob(LaunchBatchJobRequest launchBatchJobRequest) throws ValidationException {
        try {
            Job job = registry.getJob(launchBatchJobRequest.getJobName());
            JobParametersBuilder builder = new JobParametersBuilder();
            builder.addLong("timestamp", System.currentTimeMillis());

            Map<String, Object> config = launchBatchJobRequest.getJobParams();
            if(config != null){
                for(String key : config.keySet()) {
                    Object value = config.get(key);
                    builder.addJobParameter(key, value, (Class) value.getClass());
                }
            }
            launcher.run(job, builder.toJobParameters());
        }catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException | JobRestartException |
                NoSuchJobException | JobParametersInvalidException e){
            throw new ValidationException(e);
        }
    }
}
