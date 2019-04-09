package com.example.demo.seria;

import java.io.Serializable;

public class Box implements Serializable {

    /**
     * Java的序列化机制是通过判断类的serialVersionUID来验证版本一致性的
     * 在进行反序列化时，JVM会把传来的字节流中的serialVersionUID与本地相应实体类的serialVersionUID进行比较，
     * 如果相同就认为是一致的，可以进行反序列化，否则就会出现序列化版本不一致的异常，即是InvalidCastException
     */
    private static final long serialVersionUID = 1L;

    private int width;
    private int height;

    /**
     * 材质:
     * transient不能或不应该被序列化
     */
    private transient String texture;

    private static String color = "blue";

    public Box(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    @Override
    public String toString() {
        return "Box{" +
                "color=" + color +
                ",width=" + width +
                ", height=" + height +
                ", texture='" + texture + '\'' +
                '}';
    }
}
