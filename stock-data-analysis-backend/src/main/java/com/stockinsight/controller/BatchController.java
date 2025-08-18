package com.stockinsight.controller;

import com.stockinsight.model.dto.stockInsight.request.EnquiryBatchJobRequest;
import com.stockinsight.model.dto.stockInsight.request.LaunchBatchJobRequest;
import com.stockinsight.model.dto.stockInsight.response.EnquiryBatchJobResponse;
import com.stockinsight.service.BatchService;
import com.ykm.common.common_lib.model.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("batch")
@AllArgsConstructor
@Validated
public class BatchController {
    private final BatchService batchService;

    @PostMapping("launch")
    public ApiResponse launchBatchJob(@Valid @RequestBody LaunchBatchJobRequest launchBatchJobRequest){
        batchService.launchBatchJob(launchBatchJobRequest);
        return new ApiResponse();
    }

    @PostMapping("enquiry")
    public EnquiryBatchJobResponse enquiry(EnquiryBatchJobRequest enquiryBatchJobRequest){
        return batchService.enquiryBatchJob(enquiryBatchJobRequest);
    }
}