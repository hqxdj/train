package com.macro.mall.tiny.config;



import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.util.List;

/**
 * MyBatis配置类
 * Created by macro on 2019/4/8.
 */
@Configuration
@MapperScan("com.macro.mall.tiny.mbg.mapper")
public class MyBatisConfig {


    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\think\\Desktop\\计划状态刷为“已终止”-20201215.xls");

        FileOutputStream fileInputStream = new FileOutputStream("C:\\Users\\think\\Desktop\\aa.txt");

        FileWriter fileWriter = new FileWriter("C:\\Users\\think\\Desktop\\aa.txt");

        ExcelReader reader = ExcelUtil.getReader(file);

        int rowCount = reader.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            if (i == 84) {
                break;
            }
            List<List<Object>> read = reader.read(i + 1);
            List<Object> list = read.get(1);
            Object objects = list.get(2);

            StringBuffer sb = new StringBuffer();
            sb.append("UPDATE pms_plan SET operate = 'COMMON',status = 'CANCEL' WHERE NO = ");
            sb.append("'").append(objects).append("'").append(";\n");
            String s = sb.toString();

            fileWriter.write(s);
            fileWriter.flush();



        }

    }

}
