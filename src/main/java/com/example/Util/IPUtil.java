package com.example.Util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPUtil {


    public static final String IP_PATTERN = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

    //TODO 如果存在多个网卡情况
    public static String getLocalIPAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    public static  boolean isIPAddress(String ip)
    {

        if (ip==null || ip.isEmpty())
            return false;
        Pattern pattern = Pattern.compile(IP_PATTERN);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}
