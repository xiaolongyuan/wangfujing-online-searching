package com.wfj.search.online.web.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <br/>create at 15-7-15
 *
 * @author liuxh
 * @since 1.0.0
 */
public class DateUtils {
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static int utc_milli_second_offset = 0;

    static {
        Calendar calendar = Calendar.getInstance();
        int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
        int dstOffset = calendar.get(Calendar.DST_OFFSET);
        utc_milli_second_offset = -(zoneOffset + dstOffset);
    }

    private DateUtils() {
        throw new AssertionError("This is a utility class. No com.wfj.platform.util.DateUtils instances for you!");
    }

    /**
     * 将MJD时间对象转换为UTC时间对象
     *
     * @param date 待转换时间对象
     * @return UTC时间对象，与输入对象不是同一个对象
     */
    public static Date mjd2Utc(Date date) {
        Date _copy = new Date();
        _copy.setTime(date.getTime() + utc_milli_second_offset);
        return _copy;
    }

    /**
     * 将MJD时间对象转换为UTC时间的字符串
     *
     * @param date 待转换时间对象
     * @return UTC时间字符串，使用默认格式：yyyy-MM-ddTHH:mm:ssZ
     * @throws ParseException
     */
    public static String mjd2UtcStr(Date date) throws ParseException {
        return mjd2UtcStr(date, DEFAULT_UTC_PATTERN);
    }

    /**
     * 将MJD时间对象转换为UTC时间的字符串
     *
     * @param date 待转换时间对象
     * @return UTC时间字符串
     * @throws ParseException
     */
    public static String mjd2UtcStr(Date date, String pattern) throws ParseException {
        return date2Str(mjd2Utc(date), pattern);
    }

    public static Date str2DateDefault(String source) throws ParseException {
        return str2Date(source, DEFAULT_PATTERN);
    }

    public static Date str2Date(String source, String pattern) throws ParseException {
        if (source == null || source.isEmpty() || pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("参数错误，请检查。");
        }
        DateFormat fmt = new SimpleDateFormat(pattern);
        return fmt.parse(source);
    }

    public static String date2StrDefault(Date source) throws ParseException {
        return date2Str(source, DEFAULT_PATTERN);
    }

    public static String date2Str(Date source, String pattern) throws ParseException {
        if (source == null || pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("参数错误，请检查。");
        }
        DateFormat fmt = new SimpleDateFormat(pattern);
        return fmt.format(source);
    }
}
