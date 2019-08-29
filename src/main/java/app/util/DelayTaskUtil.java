package app.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
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

        Thread daemonThread = new Thread(() -> {
            while (true) {
                try {
                    DelayedTask task = delayQueue.poll(1, TimeUnit.HOURS);
                    if (task != null) {
                        log.info("执行DelayedTask:{}", task);
                        executorService.execute(task);
                    }
                } catch (Exception e) {
                    log.error("执行DelayedTask发生异常", e);
                }
            }
        });

        daemonThread.setDaemon(true);
        daemonThread.setName("Delay-Daemon");
        daemonThread.start();
    }


    @Data
    public static class DelayedTask implements Runnable, Delayed {

        /**
         * 延时毫秒数
         */
        private long delayedMilliSeconds;
        /**
         * 任务创建时的系统毫秒值
         */
        private long currentTimeMillisOfCreated;

        /**
         * 任务对象
         */
        private Runnable target;


        /**
         * 获取延时任务实例
         *
         * @param delayedMilliSeconds 延时时间
         * @param target              延时任务
         * @author Huanqd@2018年9月20日 上午11:08:31
         */
        private DelayedTask(long delayedMilliSeconds, Runnable target) {
            this.currentTimeMillisOfCreated = System.currentTimeMillis();
            this.delayedMilliSeconds = delayedMilliSeconds;
            this.target = target;
        }

        @Override
        public void run() {
            if (target != null) {
                target.run();
            }
        }

        /**
         * 获取当前任务的剩余延时
         *
         * @param unit 单位
         */
        @Override
        public long getDelay(TimeUnit unit) {
            long currentDelay = this.currentTimeMillisOfCreated + this.delayedMilliSeconds - System.currentTimeMillis();
            return unit.convert(currentDelay, TimeUnit.MILLISECONDS);
        }

        /**
         * 用于队列排队
         */
        @Override
        public int compareTo(Delayed other) {
            int result = 0;
            if (other instanceof DelayedTask) {
                DelayedTask otherTask = (DelayedTask) other;
                // 当前任务执行时间
                long selfExecTime = this.currentTimeMillisOfCreated + this.delayedMilliSeconds;
                // 其余任务执行时间
                long otherExecTime = otherTask.getCurrentTimeMillisOfCreated() + otherTask.getDelayedMilliSeconds();
                result = Long.compare(selfExecTime, otherExecTime);
            }
            return result;
        }
    }
}
