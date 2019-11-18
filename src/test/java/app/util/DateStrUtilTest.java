package app.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author faith.huan 2019-11-18 13:58
 */
public class DateStrUtilTest {

    @Test
    public void getDateTimeStringNow() {
        System.out.println(DateStrUtil.getDateTimeStringNow());
        System.out.println(DateStrUtil.getDateStringNow());
        System.out.println(DateStrUtil.getTimeStringNow());

    }
}
