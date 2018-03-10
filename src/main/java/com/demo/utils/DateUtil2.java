package com.demo.utils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class DateUtil2 {

    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneOffset.UTC);
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC);
    public static final DateTimeFormatter DEFAULT_EXPIRES_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz")
            .withZone(ZoneOffset.UTC).withLocale(Locale.ENGLISH);
    public static final DateTimeFormatter GUANZHOU_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneOffset.ofHours(+8));
    public static final DateTimeFormatter GUANZHOU_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            .withZone(ZoneOffset.ofHours(+8));

    public static String nowUTCDateTime() {
        return formatDateTime(nowUTCTimestamp());
    }

    public static long nowUTCTimestamp() {
        // System.currentTimeMillis is utc base
        return System.currentTimeMillis();
    }

    public static String formatDate(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.UTC).format(DEFAULT_DATE_FORMATTER);
    }

    public static String formatDate(long timestamp, int zoneOffsetTotalSeconds) {
        if (zoneOffsetTotalSeconds == 0) {
            return formatDate(timestamp);
        }
        return Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.UTC)
                .format(DEFAULT_DATE_FORMATTER.withZone(ZoneOffset.ofTotalSeconds(zoneOffsetTotalSeconds)));
    }

    public static String formatGuanzhouDate(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.UTC).format(GUANZHOU_DATE_FORMATTER);
    }

    public static String formatDateTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.UTC).format(DEFAULT_DATE_TIME_FORMATTER);
    }

    public static String formatGuanzhouDateTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.UTC).format(GUANZHOU_DATE_TIME_FORMATTER);
    }

    public static String formatExpiresDateTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.UTC).format(DEFAULT_EXPIRES_DATE_TIME_FORMATTER);
    }

    public static Long parseDate(String strDate) {
        if (StringUtil.isNullOrEmpty(strDate)) {
            return null;
        }
        return parseDateTime(StringUtil.concat(strDate, " 00:00:00"));
    }

    public static Long parseDateTime(String strDateTime) {
        if (StringUtil.isNullOrEmpty(strDateTime)) {
            return null;
        }
        try {
            return LocalDateTime.parse(strDateTime, DEFAULT_DATE_TIME_FORMATTER).toEpochSecond(ZoneOffset.UTC) * 1000;
        } catch (DateTimeParseException ex) { // ignore exception
        }
        return null;
    }

    public static Long parseDateOrDateTime(String strDateOrDateTime) {
        if (StringUtil.isNullOrEmpty(strDateOrDateTime)) {
            return null;
        }
        strDateOrDateTime = strDateOrDateTime.trim();
        int len = strDateOrDateTime.length();
        if (len == 10) { // yyyy-MM-dd
            return parseDate(strDateOrDateTime);
        } else if (len == 19) { // yyyy-MM-dd HH:mm:ss
            return parseDateTime(strDateOrDateTime);
        } else {
            throw new IllegalArgumentException("length doesn't match");
        }
    }

    public static String reformatDate(String strDateOrDateTime) {
        try {
            Long milli = parseDateOrDateTime(strDateOrDateTime);
            if (milli != null) {
                return formatDate(milli);
            }
        } catch (IllegalArgumentException ex) { // ignore exception
        }
        return "";
    }

    public static float nanoSecondToSecond(long nanoSecond) {
        if (nanoSecond == 0) {
            return 0;
        }
        double second = TimeUnit.MILLISECONDS.convert(nanoSecond, TimeUnit.NANOSECONDS) / 1000.0;
        BigDecimal bd = BigDecimal.valueOf(second).setScale(3, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

}