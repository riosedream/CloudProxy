package com.cm.proxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bighero on 2019/3/17.
 */
public class Request {
    String url;
    String method;
    String protocol;

    Map<String, String> headers = new HashMap<String, String>();

    String body;

    public static Request build() {
        Request request = new Request();
        return request;
    }

    public void parser(String line) {
        if (line == null || line.trim().equals("")) {
            return;
        }
        String[] array = line.split(" ");
        if (array.length == 3) {
            method = array[0];
            url = array[1];
            protocol = array[2];
        } else {
            for (String s : array) {
                if (s.contains("GET") || s.contains("POST")) {
                    method = s;
                } else if (s.contains("/")) {
                    url = s;
                } else if (s.contains("HTTP")) {
                    protocol = s;
                }
            }
        }

        if (method == null) {
            method = "GET";
        }
    }

    public void headers(String line) {
        if (line == null || !line.contains(":")) {
            return;
        }
        String key = line.substring(0, line.indexOf(":")).trim();
        String value = line.substring(line.indexOf(":") + 1).trim();
        headers.put(key, value);
    }

    public String getMethod() {
        if (method.equals("CONNECT")) {
            if (body == null) {
                return "GET";
            } else {
                return "POST";
            }
        }
        return method;
    }

    public String getUrl() {
        if (!url.contains("http")) {
            if (url.contains("443")) {
                url = "https://" + url;
            } else if (url.contains("80")) {
                url = "http://" + url;
            } else {
                if (headers.containsKey("HOST")) {
                    String host = headers.get("HOST");
                    if (host.contains("443")) {
                        url = "https://" + url;
                    } else if (host.contains("80")) {
                        url = "http://" + url;
                    }
                }
            }
        }
        return url;
    }

    @Override
    public String toString() {
        return "Request{" +
                "url='" + getUrl() + '\'' +
                ", method='" + getMethod() + '\'' +
                ", protocol='" + protocol + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
