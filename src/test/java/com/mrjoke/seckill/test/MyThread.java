package com.mrjoke.seckill.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class MyThread extends Thread {
    private int num;
    private Random random = new Random();
    private final String url = "http://localhost:8080/seckill/4/b0d35777ad2f4a511e346f4abd54162d/execution";

    public MyThread(int num) {
        this.num = num;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1; i++) {
            try {
                URL http = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) http.openConnection();
                int rand = random.nextInt(100_000_000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestProperty("Cookie", "killPhone=135" + rand + "; _ga=GA1.1.990971867.1502258286; _gid=GA1.1.935466455.1505102284; JSESSIONID=B6FFE75556090807F36388F083779364");
                urlConnection.setDoInput(true);
                if (urlConnection.getResponseCode() == 200) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    System.out.println("Thread " + num + " : " + stringBuffer.toString() + ":::" + i);
                    bufferedReader.close();
                } else {
                    System.out.println(urlConnection.getResponseCode());
                }
                urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
