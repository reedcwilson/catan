package com.catan.main.persistence;

import org.apache.commons.lang.SerializationUtils;

import java.io.Serializable;

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

    public static byte[] serialize(Serializable obj) {
        return SerializationUtils.serialize(obj);
    }
    public static Object deserialize(byte[] bytes) {
        return SerializationUtils.deserialize(bytes);
    }
}
