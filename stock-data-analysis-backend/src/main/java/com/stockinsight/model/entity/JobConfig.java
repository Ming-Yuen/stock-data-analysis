package com.stockinsight.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ykm.orm.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "uk_job_config",
                columnNames = {"task_name","task_group"}
        )
)
public class JobConfig extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_config_seq")
    @SequenceGenerator(name = "job_config_seq", sequenceName = "job_config_id_seq", allocationSize = 50)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "task_name", nullable = false, length = 100)
    private String taskName;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_group", nullable = false, length = 50)
    private TaskGroup taskGroup = TaskGroup.DEFAULT;

    @Lob
    @Column(name = "task_description")
    private String taskDescription;

    @Column(name = "job_class_name", nullable = false, length = 255)
    private String jobClassName;

    @Column(name = "cron_expression", length = 100)
    private String cronExpression;

    @Enumerated(EnumType.STRING)
    @Column(name = "trigger_type", nullable = false, length = 20)
    private TriggerType triggerType = TriggerType.CRON;

    @Column(name = "config_json", length = 3000)
    private String configJson;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = Boolean.TRUE;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount = 0;

    @Column(name = "max_retry_count", nullable = false)
    private Integer maxRetryCount = 3;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TaskStatus status = TaskStatus.WAITING;

    public enum TriggerType { CRON, SIMPLE, CALENDAR }
    public enum TaskStatus { WAITING, RUNNING, PAUSED, COMPLETE, ERROR }
    public enum TaskGroup { DEFAULT, SCHEDULE, BATCH, REPORT }
}
