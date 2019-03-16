package com.cm.common.utils.host;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostUtil {
    public static InetAddress getLocalServer () throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress;
    }
}
