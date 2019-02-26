package app.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author faith.huan 2019-02-26 17:48
 */
public class MdcUtil {

    public static void setMdcUUID() {
        MDC.put("TID", UUID.randomUUID().toString());
    }

    public static void setMdc() {
        MDC.put("TID", RandomStringUtils.random(10, "0123456789"));
    }

    public static void setMdc(int length) {
        MDC.put("TID", RandomStringUtils.random(length, "0123456789"));
    }

    public static void setMdc(int length, String prefix) {
        MDC.put("TID", prefix + RandomStringUtils.random(length, "0123456789"));
    }

    public static void setMdc(int length, String prefix, String subfix) {
        MDC.put("TID", prefix + RandomStringUtils.random(length, "0123456789") + subfix);
    }

    public static void clearMdc() {
        MDC.put("TID", null);
    }

    public static String getMdc(){
        return MDC.get("TID");
    }

}
