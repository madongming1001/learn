package com.madm.learnroute.javaee;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Data
@AllArgsConstructor
class CallerTask implements Callable<Integer> {

    private Integer id;

    @Override
    public Integer call() {
        int i = 10 / 0;
        return id;
    }
}

public class ThreadCreationPractice {

    public static void main(String[] args) {
        List<FutureTask<Integer>> futureTaskList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FutureTask<Integer> futureTask = new FutureTask<>(new CallerTask(i));
            futureTaskList.add(futureTask);
            futureTask.run();
            try {
                futureTask.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }finally {
                System.out.println("走到了finally语句快");
            }
        }
        futureTaskList.stream().forEach(f -> {
            try {
                System.out.println("线程号为：" + f.get() + " 当前线程为：" + Thread.currentThread().getName());
            } catch (Exception e) {
            }
        });
//        Thread thread = new Thread(new RunnableTrash());
//        try {
//            thread.run();
//        } finally {
//            System.out.println("走到了finally 语句块");
//        }
    }

    public class ExpensiveObject {
    }

    @NotThreadSafe
    public class LazyInitRaceO {
        private ExpensiveObject instance = null;

        public ExpensiveObject getInstance() {
            if (instance == null) {
                instance = new ExpensiveObject();
            }
            return instance;
        }
    }
}


@SuppressWarnings(value = {"unused", "Tash"})
class RunnableTrash implements Runnable {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        System.out.println("I am a child thread");
        int i = 1 / 0;
    }
}
