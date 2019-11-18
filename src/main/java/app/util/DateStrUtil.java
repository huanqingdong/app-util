package app.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期字符串工具类
 *
 * @author faith.huan 2019-11-18 13:26
 */
public class DateStrUtil {

    private static final SimpleDateFormat SDF_STD_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat SDF_STD_DATE = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat SDF_STD_TIME = new SimpleDateFormat("HH:mm:ss");

    /**
     * 根据传入date,返回日期_时间字符串  2019-10-01 09:11:12
     *
     * @param date 日期
     * @return 日期字符串 2019-10-01 09:11:12
     */
    public static synchronized String getDateTimeString(Date date) {
        return SDF_STD_DATE_TIME.format(date);
    }

    /**
     * 获取当前日期对应的日期_时间字符串 2019-10-01 09:11:12
     *
     * @return 日期字符串  2019-10-01 09:11:12
     */
    public static String getDateTimeStringNow() {
        return getDateTimeString(new Date());
    }


    /**
     * 根据传入date,返回日期字符串  2019-10-01
     *
     * @param date 日期
     * @return 日期字符串
     */
    public static synchronized String getDateString(Date date) {
        return SDF_STD_DATE.format(date);
    }

    /**
     * 获取当前日期对应的日期字符串 2019-10-01
     *
     * @return 日期字符串
     */
    public static String getDateStringNow() {
        return getDateString(new Date());
    }


    /**
     * 根据传入date,返回时间字符串  09:11:12
     *
     * @param date 日期
     * @return 日期字符串
     */
    public static synchronized String getTimeString(Date date) {
        return SDF_STD_TIME.format(date);
    }

    /**
     * 获取当前日期对应的时间字符串 09:11:12
     *
     * @return 时间字符串
     */
    public static String getTimeStringNow() {
        return getTimeString(new Date());
    }

}
