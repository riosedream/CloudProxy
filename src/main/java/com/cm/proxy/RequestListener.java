package com.cm.proxy;

import com.alibaba.fastjson.JSONObject;

public interface RequestListener {
    void onFinish(String responseStr);
}
