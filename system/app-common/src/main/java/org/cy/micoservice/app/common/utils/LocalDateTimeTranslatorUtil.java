package org.cy.micoservice.app.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @Author: Lil-K
 * @Date: 2025/12/28
 * @Description: 时间转换工具
 */
public class LocalDateTimeTranslatorUtil {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public static String translate(LocalDateTime dateTime) {
    ZoneId eastEightZone = ZoneId.of("Asia/Shanghai");
    LocalDateTime localDateTime = Instant.ofEpochMilli(System.currentTimeMillis())
      .atZone(eastEightZone)
      .toLocalDateTime();
    LocalDateTime now = localDateTime.now();
    long years = ChronoUnit.YEARS.between(dateTime, now);
    if (years > 0) {
      return years + "年前";
    }
    long months = ChronoUnit.MONTHS.between(dateTime, now);
    if (months > 0) {
      return months + "个月前";
    }
    long days = ChronoUnit.DAYS.between(dateTime, now);
    if (days > 0) {
      return days + "天前";
    }
    long hours = ChronoUnit.HOURS.between(dateTime, now);
    if (hours > 0) {
      return hours + "小时前";
    }
    long minutes = ChronoUnit.MINUTES.between(dateTime, now);
    if (minutes > 0) {
      return minutes + "分钟前";
    }
    long seconds = ChronoUnit.SECONDS.between(dateTime, now);
    if (seconds > 0) {
      return seconds + "秒前";
    }
    return "刚刚";
  }
}