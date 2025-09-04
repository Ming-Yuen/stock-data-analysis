package com.stockinsight.init;

import com.stockinsight.config.QuartzConfig;
import com.stockinsight.model.entity.Exchange;
import com.stockinsight.model.entity.JobConfig;
import com.stockinsight.model.entity.Menu;
import com.stockinsight.repository.MenuRepository;
import com.stockinsight.repository.ExchangeRepository;
import com.stockinsight.repository.JobConfigRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

@Data
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final QuartzConfig quartzConfig;
    private final MenuRepository menuRepository;
    private final ExchangeRepository exchangeRepository;
    private final JobConfigRepository jobConfigRepository;

    @Override
    @Transactional
    public void run(String... args) {
        initializeMenuData();
        initializeExchangeData();
        initializeJobConfigData();
        quartzConfig.registerDatabaseTasks();
    }

    private void initializeMenuData() {
        if (menuRepository.count() == 0) {
            menuRepository.saveAll(Arrays.asList(
                    createMenu(1, "Home", "home", "/", null),
                    createMenu(2, "User", "account_circle", "/user", null),
                    createMenu(3, "Stock Analysis", "analytics", "/stockAnalysis", null),
                    createMenu(4, "Data Management", "storage", "/dataManagement", null),
                    createMenu(5, "Job Configuration", "jobConfig", "/JobConfig", 4)
            ));
        }
    }

    private Menu createMenu(Integer menuId, String name, String icon, String path, Integer parentId) {
        Menu menu = new Menu();
        menu.setMenuId(menuId);
        menu.setName(name);
        menu.setIcon(icon);
        menu.setPath(path);
        menu.setParentId(parentId);
        // 审计字段由 Spring Data JPA 自动维护
        return menu;
    }

    private void initializeExchangeData() {
        if (exchangeRepository.count() == 0) {
            Exchange usExchange = new Exchange();
            usExchange.setExchangeCode("US");
            usExchange.setName("US exchanges (NYSE, Nasdaq)");
            usExchange.setMic("XNYS,XASE,BATS,ARCX,XNMS,XNCM,XNGS,IEXG,XNAS,OTCM,OOTC");
            usExchange.setTimezone("America/New_York");
            // 其他字段由实体默认值或审计自动填充
            exchangeRepository.save(usExchange);
        }
    }

    private void initializeJobConfigData() {
        if (jobConfigRepository.count() == 0) {
            JobConfig jobConfig = new JobConfig();
            jobConfig.setTaskName("StockDailyTask");
            jobConfig.setTaskGroup(JobConfig.TaskGroup.SCHEDULE);
            jobConfig.setTaskDescription("Daily task to update stock symbols for all exchanges");
            jobConfig.setJobClassName("com.stockinsight.scheduling.StockDailyTask");
            jobConfig.setCronExpression("0 0 6 * * ?");
            jobConfig.setTriggerType(JobConfig.TriggerType.CRON);
            jobConfig.setConfigJson("{}");
            jobConfig.setEnabled(true);
            jobConfig.setStartTime(LocalDateTime.now());
            jobConfig.setRetryCount(0);
            jobConfig.setMaxRetryCount(3);
            jobConfig.setStatus(JobConfig.TaskStatus.WAITING);
            // 审计字段由 Spring Data JPA 自动维护
            jobConfigRepository.save(jobConfig);
        }
    }
}
