package com.macro.mall.tiny.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import org.springframework.stereotype.Component;

@Component
public class MyTestJob extends IJobHandler {



    @Override
    public ReturnT<String> execute(String param) throws Exception {
        System.out.println("hello job" + "param" + param);
        return SUCCESS;
    }
}
