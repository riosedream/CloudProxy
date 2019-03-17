package com.cm.proxy;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: ssm
 * @description: Proxy Server端
 * @author: DongKe
 * @create: 2019-03-12 21:18
 **/
public class ServerTest {
    private static String serverResponse = "";
    private static boolean finishFlag = false;
    private static ExecutorService executorService;


    public static void main(String[] args) throws IOException {
        executorService = Executors.newFixedThreadPool(4);
        ServerSocket serverSocket = new ServerSocket(8077);
        System.out.println("Wait for connect...");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Connected!!!");
            ProcessSocket processSocket = new ProcessSocket(socket);
            executorService.execute(processSocket);

        }
    }


    /**
     * 处理client端数据的
     */
    static class ProcessSocket implements Runnable {
        Socket processSocket;
        String proxyHost = "";

        // 构造
        public ProcessSocket(Socket socket) {
            this.processSocket = socket;
        }

        @Override
        public void run() {
            try {
                if (this.processSocket == null) {
                    return;
                }
                InputStream inputStream = processSocket.getInputStream();// 从socket获取输入流
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");// 用InputStreamReader 读取 InputStream
                BufferedReader reader = new BufferedReader(inputStreamReader);// 用BufferReader读取InputStreamReader中内容
                Request request = Request.build();
                String first = reader.readLine();
                request.parser(first);
                for (String temp = reader.readLine(); temp != null && !temp.equals(""); temp = reader.readLine()) {
                    request.headers(temp);
                }
                StringBuilder stringBuilder = null;
                for (String temp = reader.readLine(); temp != null; temp = reader.readLine()) {

                    if (!temp.equals("")) {
                        if (stringBuilder == null) {
                            stringBuilder = new StringBuilder();
                        }
                        stringBuilder.append(temp).append("\n");
                    }
                }
                if (stringBuilder != null) {
                    request.body = stringBuilder.toString();
                }
                System.out.println(String.format("-------- request: %s", request.toString()));
                ProxyConnect proxyConnect = new ProxyConnect(request, processSocket);
                proxyConnect.run();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

