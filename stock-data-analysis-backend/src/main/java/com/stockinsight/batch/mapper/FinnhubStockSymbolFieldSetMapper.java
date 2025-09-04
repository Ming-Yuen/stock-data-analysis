package com.stockinsight.batch.mapper;

import com.stockinsight.model.entity.Exchange;
import com.ykm.common.common_lib.converter.CommonTypeConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FinnhubStockSymbolFieldSetMapper implements FieldSetMapper<Exchange>, CommonTypeConverter {
    @Override
    public Exchange mapFieldSet(FieldSet fieldSet) throws BindException {
        Exchange dto = new Exchange();
        dto.setExchangeCode(fieldSet.readString("code"));
        dto.setName(fieldSet.readString("name"));
        dto.setMic(fieldSet.readString("mic"));
        dto.setTimezone(fieldSet.readString("timezone"));
        String preMarket = fieldSet.readString("pre_market");
        String today = LocalDate.now().toString();
        if(StringUtils.isNoneBlank(preMarket)) {
            String[] preMarketDatetime = preMarket.split("-");
            
            dto.setPreMarketStartTime(LocalDateTime.parse(today + "T" + preMarketDatetime[0] + ":00"));
            dto.setPreMarketEndTime(LocalDateTime.parse(today + "T" + preMarketDatetime[1] + ":00"));
        }
        String regularMarket = fieldSet.readString("hour");
        if(StringUtils.isNotBlank(regularMarket)) {
            String[] regularMarketDatetime = regularMarket.split("-");
            dto.setRegularMarketStartTime(LocalDateTime.parse(today + "T" + regularMarketDatetime[0] + ":00"));
            dto.setRegularMarketEndTime(LocalDateTime.parse(today + "T" + regularMarketDatetime[1] + ":00"));
        }
        String postMarket = fieldSet.readString("post_market");
        if(StringUtils.isNotBlank(postMarket)) {
            String[] postMarketDatetime = postMarket.split("-");
            dto.setPostMarketStartTime(LocalDateTime.parse(today + "T" + postMarketDatetime[0] + ":00"));
            dto.setPostMarketEndTime(LocalDateTime.parse(today + "T" + postMarketDatetime[1] + ":00"));
        }
        dto.setCloseDate(fieldSet.readString("close_date"));
        dto.setCountry(fieldSet.readString("country_name"));
        dto.setReference(fieldSet.readString("reference"));
        return dto;
    }
}
