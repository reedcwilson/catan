package com.catan.main.persistence;

import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
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
    public static void delete(File file) {
        if(file.isDirectory()){
            //directory is empty, then delete it
            if(file.list().length==0){
                file.delete();
                System.out.println("Directory is deleted : " + file.getAbsolutePath());
            }else{
                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if(file.list().length==0){
                    file.delete();
                    System.out.println("Directory is deleted : "
                            + file.getAbsolutePath());
                }
            }
        }else{
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }
}
