package app.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author f
 * aith.huan 2019-08-29 18:12
 */
@Slf4j
public class DelayTaskUtilTest {

    @Test
    public void addTask() throws InterruptedException {

        DelayTaskUtil.addTask(1000*2, ()->{
            log.info("delay 2s");
        });


        DelayTaskUtil.addTask(1000*4, ()->{
            log.info("delay 4s");
        });


        DelayTaskUtil.addTask(1000*6, ()->{
            log.info("delay 6s");
        });

        Thread.sleep(1000*10);
    }
}
