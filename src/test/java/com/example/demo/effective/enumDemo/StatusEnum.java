package com.example.demo.effective.enumDemo;

public enum StatusEnum {
    APPLY(1),ACTIVE(2),FREEZE(3),FREE(4);

    private final int index;

    StatusEnum(int size){
        this.index = size;
    }

    public void method(){
        //System.out.println(this.ordinal()); 位置顺序变动，会乱套
        System.out.println(index);
    }

    public void apply1(int status){
        System.out.println(status);
    }
}
