package com.stockinsight.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Component
@Validated
@ConfigurationProperties(prefix = "finnhub")
public class FinnhubProperties {
    @NotBlank
    private String baseUrl;
    @NotBlank
    private String apiKey;
    @NotNull
    private Integer timeout;
//    @NotNull
//    private Endpoints endpoints;
//    public Integer getTimeout(){
//        if(endpoints != null){
//            Integer endPointTimeout = endpoints.getStockSymbol().timeout;
//            if(endPointTimeout != null){
//                return endPointTimeout;
//            }
//        }
//        return globalTimeout;
//    }
//    @Data
//    public static class Endpoints {
//        @NotNull
//        private Properties stockSymbol;
//        @NotNull
//        private Properties news;
//    }
//    @Data
//    public static class Properties{
//        private String path;
//        @Getter(AccessLevel.NONE)
//        private Integer timeout;
//    }
}
