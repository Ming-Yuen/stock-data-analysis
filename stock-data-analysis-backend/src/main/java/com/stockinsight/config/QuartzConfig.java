package com.stockinsight.config;

import com.stockinsight.model.entity.JobConfig;
import com.stockinsight.repository.JobConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

	private final Scheduler scheduler;
	private final JobConfigRepository repository;

	@PostConstruct
	public void registerDatabaseTasks() {
		try {
			List<JobConfig> tasks = repository.findByEnabledTrue();
			log.info("Loading {} enabled tasks from database", tasks.size());

			for (JobConfig task : tasks) {
				registerOrUpdateTask(task);
			}

			log.info("Successfully registered all database tasks");
		} catch (Exception e) {
			log.error("Failed to register database tasks", e);
			throw new RuntimeException("Quartz initialization failed", e);
		}
	}

	private void registerOrUpdateTask(JobConfig task) throws Exception {
		try {
			Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(task.getJobClassName());

			JobKey jobKey = JobKey.jobKey(task.getTaskName(), task.getTaskGroup().name());
			TriggerKey triggerKey = TriggerKey.triggerKey(task.getTaskName() + "-trigger", task.getTaskGroup() + "-triggers");

			// 构建 JobDetail
			JobDetail jobDetail = JobBuilder.newJob(jobClass)
					.withIdentity(jobKey)
					.withDescription(task.getTaskDescription())
					.storeDurably()
					.build();

			// 构建 CronTrigger，设置错过时立即执行
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
					.cronSchedule(task.getCronExpression())
					.withMisfireHandlingInstructionFireAndProceed();

			CronTrigger newTrigger = TriggerBuilder.newTrigger()
					.withIdentity(triggerKey)
					.forJob(jobDetail)
					.withSchedule(scheduleBuilder)
					.build();

			if (scheduler.checkExists(jobKey)) {
				scheduler.addJob(jobDetail, true);
				scheduler.rescheduleJob(triggerKey, newTrigger);
			} else {
				scheduler.scheduleJob(jobDetail, newTrigger);
			}
		} catch (ClassNotFoundException e) {
			log.error("Job class not found for task: {} - {}", task.getTaskName(), task.getJobClassName(), e);
			throw new RuntimeException("Invalid job class: " + task.getJobClassName(), e);
		} catch (SchedulerException e) {
			log.error("Failed to register task: {}", task.getTaskName(), e);
			throw e;
		}
	}
}