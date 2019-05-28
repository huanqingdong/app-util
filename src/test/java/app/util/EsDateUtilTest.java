package app.util;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author faith.huan 2019-05-28 14:21
 */
public class EsDateUtilTest {

    @org.junit.Test
    public void getEsDateString() {
        System.out.println(EsDateUtil.getEsDateString(new Date()));

    }

    @org.junit.Test
    public void getEsDateStringNow() {
        System.out.println(EsDateUtil.getEsDateStringNow());
    }
}
