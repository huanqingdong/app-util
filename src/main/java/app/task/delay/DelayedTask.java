package app.task.delay;

import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Data
public class DelayedTask implements Runnable, Delayed {

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
    public DelayedTask(long delayedMilliSeconds, Runnable target) {
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
