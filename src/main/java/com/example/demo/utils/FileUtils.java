package com.example.demo.utils;

import com.alibaba.fastjson.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件工具类
 */
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
        throw new AssertionError();
    }

    /**
     * 根据路径创建文件，如果路径文件夹不存在，就创建
     *
     * @param filePath
     * @return
     */
    public static File createFileAbsolute(String filePath) throws IOException {
        File file = new File(filePath);
        File fileParent = file.getParentFile();

        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        file.createNewFile();
        return file;
    }

    /**
     * 传统写法，JDK1.6
     *
     * @param dataStr
     * @param filePath
     * @return
     */
    public static long write2FileTradition(String dataStr, String filePath) {
        long fileSize = 0L;
        RandomAccessFile raf = null;
        FileChannel fchannel = null;
        try {
            raf = new RandomAccessFile(createFileAbsolute(filePath), "rw");
            fchannel = raf.getChannel();

            ByteBuffer buf = ByteBuffer.allocate(1024);

            buf.clear();
            buf.put(dataStr.getBytes());
            buf.flip();

            while (buf.hasRemaining()) {
                fchannel.write(buf);
            }

            fileSize = fchannel.size();

        } catch (FileNotFoundException e) {
            logger.error("file not found", e);
        } catch (IOException e) {
            logger.error("IO exception", e);
        } finally {
            IOUtils.close(fchannel);
            IOUtils.close(raf);
        }
        return fileSize;
    }

    /**
     * JDK1.7 关闭资源语法糖
     *
     * @param dataStr
     * @param filePath
     * @return
     */
    public static long write2File(String dataStr, String filePath) {
        long fileSize = 0L;
        try (
                RandomAccessFile raf = new RandomAccessFile(createFileAbsolute(filePath), "rw");
                FileChannel fchannel = raf.getChannel()
        ) {
            ByteBuffer buf = ByteBuffer.allocate(1024);

            buf.clear();
            buf.put(dataStr.getBytes());
            buf.flip();

            while (buf.hasRemaining()) {
                fchannel.write(buf);
            }

            fileSize = fchannel.size();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileSize;
    }

    public static void main(String[] args) throws IOException {
        String str = "我是传奇ccc，和以色列恢复关系不会影响保加利亚和其他国家的传统友谊。";
        String filePath = ProjectPathUtils.getRootPath("/file/test.txt");
        long fileSize = FileUtils.write2File(str, filePath);
        System.out.println("fileSize = " + fileSize);
    }
}
