package com.example.demo.bloomFilter;

import java.util.BitSet;

public class BloomFilter {
    /**
     * 默认长度  2 * Math.pow(2,24)
     */
    private static final int DEFAULT_SIZE = 2 << 24;

    /**
     * 为质数，减少碰撞，原因：
     * 3： 0011
     * 5： 0101
     */
    private static final int seeds[] = new int[]{3, 5, 7, 9, 11, 13, 17, 19};
    private static Hash[] hashAr = new Hash[8];

    static {
        for (int i = 0; i < seeds.length; i++) {
            hashAr[i] = new Hash(seeds[i]);
        }
    }

    /**
     * hash方法结果记录到bitSet
     */
    private BitSet bitSet = new BitSet(DEFAULT_SIZE);

    /**
     * 将String经过Hash,结果放入bitSet
     *
     * @param content
     */
    public void add(String content) {
        for (Hash h : hashAr) {
            bitSet.set(h.getHash(content));
        }
    }

    /**
     * 是否包含
     *
     * @param content
     * @return
     */
    public boolean contains(String content) {
        boolean have = true;
        for (Hash hash : hashAr) {
            have &= bitSet.get(hash.getHash(content));
        }
        return have;
    }

    public static void main(String[] args) {
        String email="xiaozhuanfeng@126.com";
        BloomFilter bloomDemo=new BloomFilter();
        System.out.println(email+"是否在列表中： "+bloomDemo.contains(email));
        bloomDemo.add(email);
        System.out.println(email+"是否在列表中： "+bloomDemo.contains(email));
        email="xiaozhuanfeng@163.com";
        System.out.println(email+"是否在列表中： "+bloomDemo.contains(email));
    }

    private static class Hash {
        private int seed = 0;

        public Hash(int seed) {
            this.seed = seed;
        }

        public int getHash(String string) {
            int val = 0;
            int len = string.length();
            for (int i = 0; i < len; i++) {

                //与质数相乘+Assic码
                val = val * seed + string.charAt(i);
            }

            //长度为（2的次幂-1），减少碰撞
            //注意：&& 和&的区别（&& 第一表达式flase,第二表达式就不执行了，所以如果类似  val &= function()要注意）
            return val & (DEFAULT_SIZE - 1);
        }
    }
}
