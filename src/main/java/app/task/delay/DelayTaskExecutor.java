package app.task.delay;

import app.util.DelayTaskUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 延时任务工具类
 * 1. 添加任务
 * 2. 守护进程执行延时任务
 *
 * @author Huanqd@2018年9月20日 上午9:23:30
 */
@Slf4j
public class DelayTaskExecutor {

    private volatile DelayQueue<DelayedTask> delayQueue;

    private ExecutorService executorService;

    /**
     * 守护线程前缀
     */
    private static final String DAEMON_THREAD_NAME_PREFIX = "delay-daemon-";

    /**
     * 守护线程序号
     */
    private static AtomicInteger DAEMON_THREAD_NAME_SERIAL = new AtomicInteger();

    public DelayTaskExecutor(ExecutorService executorService) {
        this.executorService = executorService;
        this.delayQueue = new DelayQueue<>();
        init();
    }

    /**
     * 初始化守护线程
     */
    private void init() {
        Thread daemonThread = DelayTaskUtil.getDelayDaemonThread(executorService, delayQueue, log);
        daemonThread.setName(DAEMON_THREAD_NAME_PREFIX + DAEMON_THREAD_NAME_SERIAL.incrementAndGet());
        daemonThread.start();
    }

    /**
     * 添加任务
     *
     * @param delayedMilliSeconds 任务延时执行毫秒数
     * @param task                任务相关处理逻辑
     * @author Huanqd@2018年9月20日 上午9:30:48
     */
    public void addTask(long delayedMilliSeconds, Runnable task) {
        this.delayQueue.put(new DelayedTask(delayedMilliSeconds, task));
    }
}
