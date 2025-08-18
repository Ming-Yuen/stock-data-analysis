package com.stockinsight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.stockinsight",
		"com.ykm.common.common_lib",
		"com.ykm.orm"
})
public class StockInsightApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockInsightApplication.class, args);
	}
}
