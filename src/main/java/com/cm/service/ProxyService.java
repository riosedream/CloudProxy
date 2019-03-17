package com.cm.service;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public interface ProxyService {
    /**
     * 代理服务器开启监听端口
     */
    JSONObject startListen(String url) throws IOException;

    /**
     *获取Client端http请求
     */

    /**
     * 开启子线程转发http请求
     */

    /**
     * 响应Client http请求
     */

}
