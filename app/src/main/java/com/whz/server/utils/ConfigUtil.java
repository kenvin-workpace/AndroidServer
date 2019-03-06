package com.whz.server.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * Created by kevin on 2018/6/22
 */
public class ConfigUtil {

    public static final int PORT_SERVER = 8080;

    public static final String GET_FILE = "/file";

    public static final String GET_IMAGE = "/image";

    public static final String POST_JSON = "/json";


    private static final Pattern IPV4_PATTERN = Pattern.compile("^(" + "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" + "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");

    private static boolean isIPv4Address(String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    /**
     * 获取本机IP地址
     */
    public static InetAddress getLocalIPAddress() {
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            if (enumeration == null) return null;

            while (enumeration.hasMoreElements()) {
                NetworkInterface nif = enumeration.nextElement();
                Enumeration<InetAddress> inetAddresses = nif.getInetAddresses();
                if (inetAddresses == null) return null;

                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && isIPv4Address(inetAddress.getHostAddress())) {
                        return inetAddress;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
