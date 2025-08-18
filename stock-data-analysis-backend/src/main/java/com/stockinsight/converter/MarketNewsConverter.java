package com.stockinsight.converter;

import com.stockinsight.model.entity.MarketNews;
import com.ykm.common.common_lib.converter.CommonTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MarketNewsConverter extends CommonTypeMapper {
    default List<MarketNews> toMarketNews(List<com.stockinsight.model.dto.finnhub.response.MarketNews> marketNews){
        if ( marketNews == null && marketNews == null ) {
            return null;
        }

        ArrayList<MarketNews> arrayList = new ArrayList<MarketNews>();
        for (com.stockinsight.model.dto.finnhub.response.MarketNews news : marketNews) {
            arrayList.add(toNews(news));
        }
        return arrayList;
    }
    @Mapping(target = "publishedAt", source = "datetime", qualifiedByName = "longToDate")
    @Mapping(target = "title", source = "headline")
    MarketNews toNews(com.stockinsight.model.dto.finnhub.response.MarketNews marketNews);
}
