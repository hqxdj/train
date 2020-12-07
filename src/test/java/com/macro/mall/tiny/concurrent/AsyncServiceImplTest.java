package com.macro.mall.tiny.concurrent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AsyncServiceImplTest {

    @Autowired
    AsyncService asyncService;

    @Test
    public void Test() {
        String name = Thread.currentThread().getName();
        System.out.println("begin main" + name);

        asyncService.writeTxt();

        Future<String> stringFuture = asyncService.taskResult();

        asyncService.execute();

        asyncService.executeSubmit();

        String s = null;
        try {
            s = stringFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(s);

        while (true) {
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}