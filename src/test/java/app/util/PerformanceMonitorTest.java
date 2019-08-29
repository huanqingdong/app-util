package app.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author faith.huan 2019-08-29 18:25
 */
public class PerformanceMonitorTest {

    @Test
    public void stat() {

        PerformanceMonitor.useStdout();
        PerformanceMonitor.begin("方法名描述");
        SleepUtil.sleep(1);
        PerformanceMonitor.end();

    }
}
