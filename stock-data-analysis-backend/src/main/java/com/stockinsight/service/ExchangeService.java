package com.stockinsight.service;

import com.stockinsight.converter.ExchangeConverter;
import com.stockinsight.repository.ExchangeRepository;
import com.stockinsight.model.entity.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeRepository exchangeRepository;
    private final ExchangeConverter exchangeConverter;

    public List<Exchange> getExchange(){
        return exchangeRepository.findAll();
    }

    public void update(Exchange exchange) {
        Exchange persisted = exchangeRepository.findByExchangeCode(exchange);
        exchangeConverter.mergeToPersistedEntity(exchange, persisted);
        exchangeRepository.save(persisted);
    }

    /**
     * 获取指定年份美股停市交易日
     * @param year 年份
     * @return 停市日期列表
     */
    public List<LocalDate> getUSStockMarketHolidays(int year) {
        return calculateHolidays(year);
    }

    /**
     * 获取指定日期的上一个美股交易日
     * @param currentDate 当前日期
     * @return 上一个交易日
     */
    public LocalDate getPreviousTradingDay(LocalDate currentDate) {
        LocalDate previousDay = currentDate.minusDays(1);

        while (isNonTradingDay(previousDay)) {
            previousDay = previousDay.minusDays(1);
        }

        return previousDay;
    }

    /**
     * 获取指定日期的下一个美股交易日
     * @param currentDate 当前日期
     * @return 下一个交易日
     */
    public LocalDate getNextTradingDay(LocalDate currentDate) {
        LocalDate nextDay = currentDate.plusDays(1);

        while (isNonTradingDay(nextDay)) {
            nextDay = nextDay.plusDays(1);
        }

        return nextDay;
    }

    /**
     * 判断指定日期是否为美股交易日
     * @param date 待检查日期
     * @return true如果是交易日，否则false
     */
    public boolean isTradingDay(LocalDate date) {
        return !isNonTradingDay(date);
    }

    /**
     * 检查指定日期是否为美股停市日
     * @param date 待检查日期
     * @return true如果是停市日，否则false
     */
    public boolean isUSStockMarketHoliday(LocalDate date) {
        List<LocalDate> holidays = getUSStockMarketHolidays(date.getYear());
        return holidays.contains(date);
    }

    /**
     * 获取指定年份假期名称映射
     * @param year 年份
     * @return 日期到假期名称的映射
     */
    public Map<LocalDate, String> getHolidayNames(int year) {
        Map<LocalDate, String> holidayNames = new HashMap<>();
        List<LocalDate> holidays = getUSStockMarketHolidays(year);

        holidayNames.put(holidays.get(0), "New Year's Day");
        holidayNames.put(holidays.get(1), "Martin Luther King Jr. Day");
        holidayNames.put(holidays.get(2), "Washington's Birthday");
        holidayNames.put(holidays.get(3), "Good Friday");
        holidayNames.put(holidays.get(4), "Memorial Day");
        holidayNames.put(holidays.get(5), "Juneteenth");
        holidayNames.put(holidays.get(6), "Independence Day");
        holidayNames.put(holidays.get(7), "Labor Day");
        holidayNames.put(holidays.get(8), "Thanksgiving Day");
        holidayNames.put(holidays.get(9), "Christmas Day");

        return holidayNames;
    }

    /**
     * 判断指定日期是否为非交易日
     * @param date 待检查日期
     * @return true如果是非交易日，否则false
     */
    private boolean isNonTradingDay(LocalDate date) {
        // 检查是否为周末
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return true;
        }

        // 检查是否为美股停市日
        return isUSStockMarketHoliday(date);
    }

    /**
     * 计算指定年份的假期日期
     * @param year 年份
     * @return 假期日期列表
     */
    private List<LocalDate> calculateHolidays(int year) {
        List<LocalDate> holidays = new ArrayList<>();

        // 1. 新年 (New Year's Day) - 1月1日
        holidays.add(adjustWeekendHoliday(LocalDate.of(year, 1, 1)));

        // 2. 马丁·路德·金纪念日 (MLK Day) - 1月第3个星期一
        holidays.add(LocalDate.of(year, 1, 1)
                .with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY)));

        // 3. 总统日 (Washington's Birthday) - 2月第3个星期一
        holidays.add(LocalDate.of(year, 2, 1)
                .with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY)));

        // 4. 耶稣受难日 (Good Friday) - 复活节前的星期五
        holidays.add(calculateGoodFriday(year));

        // 5. 阵亡将士纪念日 (Memorial Day) - 5月最后一个星期一
        holidays.add(LocalDate.of(year, 5, 31)
                .with(TemporalAdjusters.lastInMonth(DayOfWeek.MONDAY)));

        // 6. 六月节 (Juneteenth) - 6月19日
        holidays.add(adjustWeekendHoliday(LocalDate.of(year, 6, 19)));

        // 7. 独立日 (Independence Day) - 7月4日
        holidays.add(adjustWeekendHoliday(LocalDate.of(year, 7, 4)));

        // 8. 劳动节 (Labor Day) - 9月第1个星期一
        holidays.add(LocalDate.of(year, 9, 1)
                .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)));

        // 9. 感恩节 (Thanksgiving Day) - 11月第4个星期四
        holidays.add(LocalDate.of(year, 11, 1)
                .with(TemporalAdjusters.dayOfWeekInMonth(4, DayOfWeek.THURSDAY)));

        // 10. 圣诞节 (Christmas Day) - 12月25日
        holidays.add(adjustWeekendHoliday(LocalDate.of(year, 12, 25)));

        Collections.sort(holidays);
        return holidays;
    }

    /**
     * 调整周末假期到工作日
     */
    private LocalDate adjustWeekendHoliday(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY) {
            return date.minusDays(1); // 调整到周五
        } else if (dayOfWeek == DayOfWeek.SUNDAY) {
            return date.plusDays(1); // 调整到周一
        }
        return date;
    }

    /**
     * 计算耶稣受难日
     */
    private LocalDate calculateGoodFriday(int year) {
        LocalDate easter = calculateEaster(year);
        return easter.minusDays(2);
    }

    /**
     * 计算复活节日期（高斯算法）
     */
    private LocalDate calculateEaster(int year) {
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int month = (h + l - 7 * m + 114) / 31;
        int day = ((h + l - 7 * m + 114) % 31) + 1;

        return LocalDate.of(year, month, day);
    }
}