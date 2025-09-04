package com.stockinsight.service;

import com.stockinsight.converter.ScheduleConverter;
import com.stockinsight.model.entity.JobConfig;
import com.stockinsight.model.dto.stockInsight.request.EnquiryBatchJobRequest;
import com.stockinsight.model.dto.stockInsight.request.LaunchJobRequest;
import com.stockinsight.model.dto.stockInsight.response.EnquiryJobResponse;
import com.stockinsight.repository.JobConfigRepository;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
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

@AllArgsConstructor
@Service
public class JobConfigService {
    private final JobLauncher launcher;
    private final JobRegistry registry;

    private final Scheduler scheduler;
    private final JobConfigRepository jobConfigRepository;
    private final ScheduleConverter scheduleConverter;

    public EnquiryJobResponse enquiryJobTask(EnquiryBatchJobRequest enquiryBatchJobRequest) {
        EnquiryJobResponse response = new EnquiryJobResponse();

        List<JobConfig> jobConfigs = jobConfigRepository.findAll();

        response.setJobTaskList(scheduleConverter.toJobTasks(jobConfigs));

        return response;
    }

    public void launchBatchJob(LaunchJobRequest launchJobRequest) throws ValidationException {
        try {
            Map<String, Object> config = launchJobRequest.getJobParams();
            String taskName = launchJobRequest.getJobName();

            if(JobConfig.TaskGroup.BATCH == launchJobRequest.getTaskGroup()) {
                Job job = registry.getJob(taskName);
                JobParametersBuilder builder = new JobParametersBuilder();
                builder.addLong("timestamp", System.currentTimeMillis());

                if (config != null) {
                    for (String key : config.keySet()) {
                        Object value = config.get(key);
                        builder.addJobParameter(key, value, (Class) value.getClass());
                    }
                }
                launcher.run(job, builder.toJobParameters());
            }else if(JobConfig.TaskGroup.SCHEDULE == launchJobRequest.getTaskGroup()){
                JobKey jobKey = JobKey.jobKey(taskName, launchJobRequest.getTaskGroup().name());
                if (!scheduler.checkExists(jobKey)) {
                    throw new ValidationException("Job not exists: " + taskName);
                }
                scheduler.triggerJob(jobKey);
            }
        }catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException | JobRestartException |
                NoSuchJobException | JobParametersInvalidException | SchedulerException e){
            throw new ValidationException(e);
        }
    }
}
