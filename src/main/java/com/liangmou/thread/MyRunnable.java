package com.liangmou.thread;

public class MyRunnable implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
//            if (Thread.interrupted()) {
//                return;
//            }
//            if (Thread.currentThread().isInterrupted()) {
//                return;
//            }
            System.out.println("sub thread ----> " + i + ", id : " + "my runnable id : "+Thread.currentThread().getId());
//            Thread.yield();

        }
    }
}
