package app.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author faith.huan 2018-12-12 8:36
 */
@Slf4j
public class SleepUtil {

    public static void sleep(long second) {
        try {
            Thread.sleep(second*1000);
        } catch (InterruptedException e) {
            log.error("sleep 异常",e);
        }
    }

    public static void sleepMillis(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("sleep 异常",e);
        }
    }
}
