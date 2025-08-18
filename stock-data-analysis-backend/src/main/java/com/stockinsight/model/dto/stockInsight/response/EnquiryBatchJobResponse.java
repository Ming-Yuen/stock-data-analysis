package com.stockinsight.model.dto.stockInsight.response;

import com.stockinsight.model.dto.stockInsight.BatchJob;
import com.ykm.common.common_lib.model.ApiResponse;
import lombok.Data;

import java.util.List;

@Data
public class EnquiryBatchJobResponse extends ApiResponse {
    private List<BatchJob> batchJobList;
}
