package com.liangmou.thread;

import lombok.SneakyThrows;
import org.apache.tomcat.util.threads.TaskThreadFactory;

import java.util.concurrent.*;

public class Main {

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 200, 10L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(600),
            new TaskThreadFactory("demo-factory", true, 5),
            new ThreadPoolExecutor.AbortPolicy()
            /*new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println(r.toString() + "is discard .");
        }
    }*/);
    @SneakyThrows
    public static void main(String[] args) {
        final Runnable myRunnable = new MyRunnable();
        threadPoolExecutor.submit(myRunnable);
//        Thread.sleep(1000);

//        final Thread thread = new Thread(myRunnable);
//        thread.setName("my-runnable-1");
//        thread.setDaemon(true);
//        final Thread thread1 = new Thread(new MyRunnable(){
//            @Override
//            public void run() {
//                for (int i = 0; i < 100; i++) {
//                    System.out.println("sub thread1 ----> " + i);
//                }
//            }
//        });

//        final Thread thread2 = new Thread(() -> {
//            for (int i = 0; i < 100; i++) {
//                System.out.println("sub thread2 ----> " + i);
//            }
//        });
//        thread.start();
//        thread.interrupt();
//        thread1.start();
//        thread2.start();
//        for (int i = 0; i < 100; i++) {
//
//            System.out.println("main thread ----> " + i);
//        }

    }
}
