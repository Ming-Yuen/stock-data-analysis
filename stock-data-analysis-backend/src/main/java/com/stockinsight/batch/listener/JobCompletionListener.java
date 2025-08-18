package com.stockinsight.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class JobCompletionListener extends JobExecutionListenerSupport {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        // 记录作业启动时间戳
        jobExecution.getExecutionContext().put("startTime", System.currentTimeMillis());

        // 初始化性能指标计数器
        jobExecution.getExecutionContext().put("processedRecords", 0L);
        jobExecution.getExecutionContext().put("errorCount", 0L);

        // 验证作业参数合法性
        JobParameters parameters = jobExecution.getJobParameters();
//        if (!parameters.containsKey("inputFile")) {
//            throw new JobParametersInvalidException("缺失必要参数inputFile");
//        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        // 计算作业持续时间
        long duration = System.currentTimeMillis() - (Long)jobExecution.getExecutionContext().get("startTime");

        // 获取处理结果指标
        long successCount = jobExecution.getStepExecutions().stream()
                .mapToLong(StepExecution::getWriteCount)
                .sum();

        // 记录汇总日志
        log.info("作业[{}]执行完成，耗时{}ms，处理成功{}条记录",
                jobExecution.getJobInstance().getJobName(),
                duration,
                successCount);
    }


}
