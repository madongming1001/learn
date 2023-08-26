package com.madm.learnroute.javaee.concurrent.juc;

import java.util.concurrent.*;

public class FutureTaskTest {

    private static final ConcurrentMap<Object, Future<String>> taskCache = new ConcurrentHashMap<Object, Future<String>>();

    public static void main(String[] args) {
    }

    private String executionTask(final String taskName) throws ExecutionException, InterruptedException {
        while (true) {
            Future<String> future = taskCache.get(taskName); // 1.1,2.1
            if (future == null) {
                Callable<String> task = () -> taskName; // 1.2创建任务
                FutureTask<String> futureTask = new FutureTask(task);
                future = taskCache.putIfAbsent(taskName, futureTask); // 1.3
                System.out.println(futureTask);
                if (future == null) {
                    future = futureTask;
                    futureTask.run(); // 1.4执行任务
                }
            }
            try {
                return future.get(); // 1.5,2.2线程在此等待任务执行完成
            } catch (CancellationException e) {
                taskCache.remove(taskName, future);
            }
        }
    }
}
