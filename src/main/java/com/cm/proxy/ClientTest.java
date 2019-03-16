package com.cm.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

/**
 * @program: ssm
 * @description: Proxy开发测试类
 * @author: DongKe
 * @create: 2019-03-11 20:21
 * https://blog.csdn.net/hanghangaidoudou/article/details/76045434
 **/
public class ClientTest {
    public static void main(String[] args) throws IOException {
        Proxy proxy = null ;// 代理服务器
        proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1",8077));  // 实例化本地代理对象,代理服务器的ip和端口
        // URL url = new URL("http://10.60.141.0:8098/testProxy");// 想要用代理服务器打开的网址
        URL url = new URL("http://www.baidu.com");// 想要用代理服务器打开的网址
        HttpURLConnection connection = (HttpURLConnection)url.openConnection(proxy);  //使用代理打开网页
        InputStream in = connection.getInputStream(); // 获取代理服务器返回的 输入流
        BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));// 初始化一个bufferReader,读取inputStream中信息
        StringBuilder sb = new StringBuilder();//初始化一个stringBuilder， 循环拼接bufferReader读取的信息
        String lin = System.getProperty("line.separator") ;
        for(String temp = br.readLine() ; temp!=null; temp = br.readLine() ){
            sb.append(temp+lin);
        }
        br.close();
        in.close();
        System.out.println(sb);
    }

}

