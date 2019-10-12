package com.daling.party.infrastructure.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class FreeIPUtils {

    private static String IP = null;
    static {
        try {
            IP = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException var1) {
            IP = "unknown-host";
        }
    }

    public static String getIP() {
        return IP;
    }
}
