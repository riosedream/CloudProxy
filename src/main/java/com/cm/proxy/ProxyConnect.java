package com.cm.proxy;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Map;

/**
 * @program: ssm
 * @description: 子线程中代理客户端访问目标URL
 * @author: DongKe
 * @create: 2019-03-14 22:02
 **/
public class ProxyConnect implements Runnable {
    Request request;
    Socket processSocket;

    public ProxyConnect(Request request, Socket socket) {
        this.request = request;
        processSocket = socket;
    }

    // 开启线程代替client端访问hostUrl
    @Override
    public void run() {
        try {
            proxyAccess();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void proxyAccess() throws IOException {
        String responseStr = "";
        if (request == null || request.url == null || request.url.isEmpty()) {
            return;
        }

        URL url = new URL(request.getUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(request.getMethod());
        if (!request.headers.isEmpty()) {
            for (Map.Entry<String, String> entry : request.headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        if (request.body != null) {
            // 获取URLConnection对象对应的输出流
            connection.setDoOutput(true);
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            // 发送请求参数
            out.print(request.body);
            // flush输出流的缓冲
            out.flush();
        }

        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            responseStr = stream2String(connection.getInputStream());
            System.out.println("ProxyConnect Response:" + responseStr);
        }

    }

    private String stream2String(InputStream stream) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(streamReader);
        StringBuilder stringBuilder = new StringBuilder();
        for (String temp = reader.readLine(); temp != null; temp = reader.readLine()) {
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }

    public interface connectListener {

    }
}
