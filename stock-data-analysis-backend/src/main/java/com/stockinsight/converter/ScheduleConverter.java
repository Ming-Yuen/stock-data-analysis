package com.stockinsight.converter;

import com.stockinsight.model.dto.stockInsight.JobTask;
import com.stockinsight.model.entity.JobConfig;
import com.ykm.common.common_lib.converter.CommonTypeConverter;
import com.ykm.common.common_lib.converter.JsonTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = JsonTypeMapper.class)
public interface ScheduleConverter extends CommonTypeConverter {
    ArrayList<JobTask> toJobTasks(List<JobConfig> jobConfigs);

    @Mapping(target = "jobName", source = "taskName")
    @Mapping(target = "jobParams", source = "configJson", qualifiedByName = "stringToMap")
    JobTask toJobTask(JobConfig jobConfig);
}
