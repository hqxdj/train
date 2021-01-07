package com.macro.mall.tiny.config;

import com.xxl.job.core.util.IpUtil;

import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        String build = build("1");
        System.out.println(build);

    }


    static String build(String... messages) {

        System.out.println(messages.length);

        return messages[0] ;
    }

}
