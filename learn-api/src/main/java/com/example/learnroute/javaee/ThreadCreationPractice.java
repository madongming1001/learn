package com.example.learnroute.javaee;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

@Data
@AllArgsConstructor
class CallerTask implements Callable<Integer> {

    private Integer id;

    @Override
    public Integer call() throws Exception {
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
        }
        futureTaskList.stream().forEach(f -> {
            try {
                System.out.println("线程号为：" + f.get());
            } catch (Exception e) {
            }
        });
    }
}


class RunableTash implements Runnable {

    @Override
    public void run() {
        System.out.println("I am a child thread");
    }
}
