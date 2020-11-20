package com.macro.mall.tiny.job;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class TestHttpUrlConnection {


    public void test() {
        BufferedReader bufferedReader = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://localhost:8090/brand/listAll");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setReadTimeout(3 * 1000);
            connection.setConnectTimeout(3 * 1000);
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept-Charset", "application/json;charset=UTF-8");


            connection.connect();


            // write requestBody
//            if (requestObj != null) {
//                String requestBody = GsonTool.toJson(requestObj);
//
//                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
//                dataOutputStream.write(requestBody.getBytes("UTF-8"));
//                dataOutputStream.flush();
//                dataOutputStream.close();
//            }

            int statusCode = connection.getResponseCode();

            // result
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            String resultJson = result.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
