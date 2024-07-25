package com.hhplus.concert.integration.concurrency;

import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedissonClient redissonClient;

    public void performTaskWithLock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        boolean isLocked = false;
        try {
            isLocked = lock.tryLock(20, 60, TimeUnit.SECONDS);
            if (isLocked) {
                // perform the task
                System.out.println("Task performed with lock");
                Thread.sleep(2000);
            } else {
                System.out.println("Could not acquire lock");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (isLocked) {
                lock.unlock();
            }
        }
    }

    @Test
    public void redis_처리_테스트() throws InterruptedException {
        String lockKey = "testLock";

        Runnable task = () -> performTaskWithLock(lockKey);
        List<Thread> threads = new ArrayList<>();

        for(int i = 0 ; i<10; i++) {
            Thread thread = new Thread(task);
            thread.start();
            threads.add(thread);
        }

        for(Thread thread : threads) {
            thread.join();
        }




    }

}

