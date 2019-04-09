package com.example.demo.seria;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

public class Ball implements Externalizable {

    private static final long serialVersionUID = 1L;

    private double radius;

    private String clour;

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getClour() {
        return clour;
    }

    public void setClour(String clour) {
        this.clour = clour;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        System.out.println("现在执行序列化方法");
        //可以在序列化时写非自身的变量
        Date d = new Date();
        System.out.println("序列化时间："+d);
        out.writeObject(d);
        //只序列化 radius,clour 变量
        out.writeObject(radius);
        out.writeObject(clour);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        System.out.println("现在执行反序列化方法");
        Date d=(Date)in.readObject();
        System.out.println("反序列化时间："+d);
        this.radius= (double) in.readObject();
        this.clour=(String)in.readObject();
    }

    @Override
    public String toString() {
        return "Ball{" +
                "radius=" + radius +
                ", clour='" + clour + '\'' +
                '}';
    }
}
