package com.example.demo.effective.generic;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class GennericTest {

    @Test
    public void test1(){
        //运行时才发现类型异常，追悔吧...
        List list = Lists.newArrayList("adc");
        getFirstEle1(list);//java.lang.String cannot be cast to java.lang.Integer
    }

    @Test
    public void test2(){
        List list = Lists.newArrayList("adc");
        getFirstEle2(list);//adc
    }

    @Test
    public void test3(){
        //结合test2() List和List<Object>主要区别：
        //1、List 时原生态类型，逃避了类型检查。List<Object>则明确告知编译器
        //2、List<String> 时List的字类，而不是List<Object>子类
        List<String> list = Lists.newArrayList("adc");
        //getFirstEle2(list);//编译报错
    }

    @Test
    public void test4(){
        List<String> list = Lists.newArrayList("adc");
        getFirstEle3(list);//adc
    }

    @Test
    public void test5(){
        List<? extends String> list1 = Lists.newArrayList("adc");
        List<String> list2 = Lists.newArrayList("ap");

        //<? extends E> 是 Upper Bound（上限） 的通配符，用来限制元素的类型的上限
        //list2 元素类型String，list1元素类型继承String，大类型->小类型，报错
        //list1.addAll(list2);
        list2.addAll(list1);
        System.out.println(list2); //[ap, adc]
    }


    @Test
    public void test6(){
        // <? super E> 是 Lower Bound（下限） 的通配符 ，用来限制元素的类型下限
        List<? super String> list1 = Lists.newArrayList("adc");
        List<String> list2 = Lists.newArrayList("ap");
        list1.addAll(list2);
        System.out.println(list1); //[ap, adc]
    }

    /**
     * List 原生态类型
     * @param list
     */
    public static void getFirstEle1(List list){
        int i = (int) list.get(0);
        System.out.println(i);
    }

    /**
     * List<Object> 从表述上，时所有Object元素都ok
     * @param list
     */
    public static void getFirstEle2(List<Object> list){
        Object i = (Object) list.get(0);
        System.out.println(i);
    }

    /**
     * <?> 可接受任意泛型，多定义在变量中
     * @param list
     */
    public static void getFirstEle3(List<?> list){
        System.out.println(list.get(0));
    }


    /**
     * <T> 表示方法要用到泛型参数,多用在类和方法上
     * @param list
     * @param <T>
     */
    public static <T> void getFirstEle4(List<T> list){
        T t = list.get(0);
        System.out.println(t);
    }
}
