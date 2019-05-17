package app.util;

/**
 * @author faith.huan 2019-05-17 13:23
 */
public class TimeUtil {

    private static long lastCurrentTimeMillis = System.currentTimeMillis();

    /**
     * 获取当前时间long,进行了防重复处理
     */
    public static synchronized long getCurrentTimeMillis() {
        long currentTimeMillis = System.currentTimeMillis();
        if (lastCurrentTimeMillis == currentTimeMillis) {
            lastCurrentTimeMillis++;
        } else {
            lastCurrentTimeMillis = currentTimeMillis;
        }
        return currentTimeMillis;
    }
}
