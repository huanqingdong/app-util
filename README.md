## 常用工具类

## DelayTaskUtil

> 用于调用延时任务,如下延时两秒执行
 ```java
DelayTaskUtil.addTask(1000 * 2, () -> {
    log.info("delay 2s");
});
```

## PerformanceMonitor
> 用于统计方法执行时间
```java
// 使用System.out输出信息,默认情况下使用slf4j
PerformanceMonitor.useStdout();
PerformanceMonitor.begin("方法名描述");
SleepUtil.sleep(1);
PerformanceMonitor.end();
```

