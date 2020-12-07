package com.macro.mall.tiny.concurrent;

import com.macro.mall.tiny.component.CancelOrderSender;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

@Slf4j
@Service
public class AsyncServiceImpl implements AsyncService {


    @Resource
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @Resource
    private CancelOrderSender cancelOrderSender;

    @Async("asyncTaskExecutor")
    @Override
    public Future<String> taskResult() {
        log.info(Thread.currentThread().getName());
        return new AsyncResult<>("this is result");
    }

    @Async("asyncTaskExecutor")
    @SneakyThrows
    @Override
    public void writeTxt() {


        log.info("enter thread and begin task");
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                Thread.sleep(6000);
            }
            String name = Thread.currentThread().getName();
            log.info("current thread {}",name+"\t"+i);
        }
    }


    @Override
    public void execute() {
        asyncTaskExecutor.execute(()->{
            String name = Thread.currentThread().getName();
            log.info("execute current thread {}", name);
            try {
                Thread.sleep(7000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },6000L);
    }

    @Override
    public void executeSubmit() {
        Future<String> future = asyncTaskExecutor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {

                return "this is submit";
            }
        });
        try {
            String o = future.get();
            System.out.println(o);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }



}
