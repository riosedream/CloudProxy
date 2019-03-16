package com.cm.proxy;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @program: ssm
 * @description: 子线程中代理客户端访问目标URL
 * @author: DongKe
 * @create: 2019-03-14 22:02
 **/
public class ProxyConnect extends Thread {
    String hostUrl = "";
    public ProxyConnect (String url) {
        this.hostUrl = url;
    }
    // 开启线程代替client端访问hostUrl
    @Override
    public void run() {
        try {
            proxyAccess(hostUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void proxyAccess(String urlHost) throws IOException {
        String responseStr = "";
        if (urlHost != null && !urlHost.isEmpty()) {
            URL url = new URL(urlHost);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");// 后期根据客户端方法设定request方法
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                responseStr= stream2String(connection.getInputStream());
                ServerTest.finishNotify(responseStr);
            }
            System.out.println("ProxyConnect Response:" + responseStr);
        }

    }
    private String stream2String (InputStream stream) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(streamReader);
        StringBuilder stringBuilder = new StringBuilder();
        for (String temp = reader.readLine(); temp != null ; temp = reader.readLine()) {
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }
    public interface  connectListener{

    }
}
