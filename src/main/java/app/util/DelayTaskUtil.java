package app.util;

import app.task.delay.DelayedTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 延时任务工具类
 * 1. 添加任务
 * 2. 守护进程执行延时任务
 *
 * @author Huanqd@2018年9月20日 上午9:23:30
 */
@Slf4j
public class DelayTaskUtil {

    private static volatile DelayQueue<DelayedTask> delayQueue;

    /**
     * 线程池的线程数
     */
    private static final int CORE_POOL_SIZE = 2;

    private DelayTaskUtil() {
    }

    /**
     * 添加任务
     *
     * @param delayedMilliSeconds 任务延时执行毫秒数
     * @param task                任务相关处理逻辑
     * @author Huanqd@2018年9月20日 上午9:30:48
     */
    public static void addTask(long delayedMilliSeconds, Runnable task) {
        getDelayQueue().put(new DelayedTask(delayedMilliSeconds, task));
    }

    /**
     * 获取delayQueue,保证只有一个
     * <p>
     * DCL 实现单例, 注意delayQueue上需要有volatile修饰符
     *
     * @author faith.huan 2019-08-29 06:05:47
     */
    private static DelayQueue<DelayedTask> getDelayQueue() {
        if (delayQueue == null) {
            synchronized (DelayTaskUtil.class) {
                if (delayQueue == null) {
                    delayQueue = new DelayQueue<>();
                    init();
                }
            }
        }
        return delayQueue;
    }

    /**
     * 初始化守护线程
     */
    private static void init() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE,
                new BasicThreadFactory.Builder().namingPattern("delay-pool-%d").build());

        Thread daemonThread = getDelayDaemonThread(executorService, delayQueue, log);
        daemonThread.setName("Delay-Daemon");
        daemonThread.start();
    }


    public static Thread getDelayDaemonThread(ExecutorService executor, DelayQueue<DelayedTask> queue, Logger logger) {
        Thread daemonThread = new Thread(() -> {
            while (true) {
                try {
                    DelayedTask task = queue.poll(1, TimeUnit.HOURS);
                    if (task != null) {
                        logger.info("执行DelayedTask:{}", task);
                        executor.execute(task);
                    }
                } catch (Exception e) {
                    logger.error("执行DelayedTask发生异常", e);
                }
            }
        });
        daemonThread.setDaemon(true);
        return daemonThread;
    }
}
