package com.stockinsight.controller;

import com.stockinsight.model.dto.stockInsight.request.EnquiryBatchJobRequest;
import com.stockinsight.model.dto.stockInsight.request.LaunchJobRequest;
import com.stockinsight.model.dto.stockInsight.response.EnquiryJobResponse;
import com.stockinsight.service.JobConfigService;
import com.ykm.common.common_lib.model.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("job")
@AllArgsConstructor
@Validated
public class JobConfigController {
    private final JobConfigService jobConfigService;

    @PostMapping("launch")
    public ApiResponse launchBatchJob(@Valid @RequestBody LaunchJobRequest launchJobRequest){
        jobConfigService.launchBatchJob(launchJobRequest);
        return new ApiResponse();
    }

    @PostMapping("enquiry")
    public EnquiryJobResponse enquiry(EnquiryBatchJobRequest enquiryBatchJobRequest){
        return jobConfigService.enquiryJobTask(enquiryBatchJobRequest);
    }
}