package com.macro.mall.tiny.concurrent;

import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.Future;


public interface AsyncService {

    void writeTxt();

    Future<String> taskResult();

    void execute();

    void executeSubmit();
}
