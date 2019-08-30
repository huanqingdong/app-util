package app.task.delay;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author faith.huan 2019-08-30 9:20
 */
@Slf4j
public class DelayTaskExecutorTest {

    @Test
    public void addTask() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        DelayTaskExecutor executor = new DelayTaskExecutor(executorService);

        DelayTaskExecutor executor1 = new DelayTaskExecutor(executorService);

        DelayTaskExecutor executor2 = new DelayTaskExecutor(executorService);

        executor.addTask(1000 * 2, () -> {
            log.info("delay 2s");
        });


        executor1.addTask(1000 * 4, () -> {
            log.info("delay 4s");
        });


        executor2.addTask(1000 * 6, () -> {
            log.info("delay 6s");
        });

        Thread.sleep(1000 * 10);

    }
}
