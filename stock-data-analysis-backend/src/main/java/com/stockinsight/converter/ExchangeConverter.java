package com.stockinsight.converter;

import com.stockinsight.model.entity.Exchange;
import com.ykm.common.common_lib.converter.CommonTypeConverter;
import com.ykm.orm.EntityConverter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExchangeConverter  extends CommonTypeConverter, EntityConverter<Exchange> {
}
