package app.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author faith.huan 2020-04-10 9:14
 */
public class ThreadPoolUtil {

    private static final int DEFAULT_QUEUE_CAPACITY = 1000;

    /**
     * 获取指定数量的线程池(采用CallerRun策略)
     *
     * @param prefix   线程池前缀
     * @param poolSize 线程池大小
     * @return 线程池
     */
    public static ThreadPoolExecutor getFixedPool(String prefix, int poolSize) {
        return getFixedPool(prefix, poolSize, DEFAULT_QUEUE_CAPACITY);
    }


    /**
     * 获取指定数量的线程池(采用CallerRun策略)
     *
     * @param prefix   线程池前缀
     * @param poolSize 线程池大小
     * @param queueCapacity 队列容量
     * @return 线程池
     */
    public static ThreadPoolExecutor getFixedPool(String prefix, int poolSize, int queueCapacity) {
        return new ThreadPoolExecutor(poolSize, poolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueCapacity),
                new ThreadFactory() {
                    int counter = 0;

                    @Override
                    public Thread newThread(Runnable runnable) {
                        return new Thread(runnable, prefix + "_" + counter++);
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy());
    }


}
