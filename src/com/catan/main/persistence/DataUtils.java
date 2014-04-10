package com.catan.main.persistence;

public class DataUtils {
    public static void crashOnException(Exception ex) {
        System.out.println(ex.getMessage());
        ex.printStackTrace();
        System.exit(1);
    }
    public static boolean checkArgument(int id) {
        return id > -1;
    }
    public static boolean checkArgument(Object obj) {
        return obj != null;
    }
}
