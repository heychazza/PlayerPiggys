package com.codeitforyou.piggys.util;

public class ArgUtil {

    public static boolean isLong(String arg) {
        try {
            Long.parseLong(arg);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
