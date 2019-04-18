package com.example.demo.utils;

import java.util.HashMap;

public class CommonCollectionUtils {

    private CommonCollectionUtils(){
        throw new AssertionError();
    }

    /**
     * 避免泛型冗长，精简代码
     * eg: HashMap<String,Map<String,Object>>
     * @param <K>
     * @param <V>
     * @return
     */
    public static<K,V> HashMap<K,V> newHashMapInstance(){
        return new HashMap<K,V>();
    }
}
