package com.example.demo.seria;


import com.alibaba.fastjson.util.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

public class SerializableUtils {

    private SerializableUtils() {
        //私有构造器，强化不可实列化
        throw new AssertionError();
    }

    public static <T> String getClassName(T obj) {
        Class c = obj.getClass();
        return c.getSimpleName();
    }

    public static File getFile(String filePath) throws IOException {
        File file = new File(filePath);
        return file;
    }

    /**
     * 序列化
     *
     * @param obj
     * @param <T>
     */
    public static <T> void doSerializable(T obj) {

        File file = null;
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            file = getFile(  "seria/"+getClassName(obj) + ".out");
            fos = new FileOutputStream(file);
            out = new ObjectOutputStream(fos);
            out.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(fos);
            IOUtils.close(out);
        }
    }

    /**
     * 反序列化
     *
     * @param filePath
     * @param <T>
     * @return
     */
    public static <T> T doDeserialization(String filePath) {
        File file = new File(filePath);
        FileInputStream fis = null;
        ObjectInputStream in = null;
        T obj = null;
        try {
            fis = new FileInputStream(file);
            in = new ObjectInputStream(fis);

            obj = (T) in.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(fis);
            IOUtils.close(in);
        }
        return obj;
    }

}
