package com.stockinsight.converter;

import com.stockinsight.model.dto.stockInsight.BatchJob;
import com.stockinsight.model.entity.BatchJonConfig;
import com.ykm.common.common_lib.converter.CommonTypeMapper;
import com.ykm.common.common_lib.converter.JsonTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = JsonTypeMapper.class)
public interface BatchConverter extends CommonTypeMapper {
    @Mapping(source = "name", target = "jobName")
    @Mapping(source = "isActive", target = "active", qualifiedByName = "intToIsActive")
    @Mapping(source = "configJson", target = "jobParams", qualifiedByName = "stringToMap")
    BatchJob jobToBatchJobListResponse(BatchJonConfig batchJonConfig);
}
