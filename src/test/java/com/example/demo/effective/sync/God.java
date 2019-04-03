package com.example.demo.effective.sync;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 上帝
 */
public class God {
    /**
     * 天国
     */
    private List<Believer> believers = Lists.newArrayList();

    public void addBeliever(Believer be){
        synchronized (believers){
            believers.add(be);
        }
    }

    public void removeBeliever(Believer be){
        synchronized (believers){
            believers.remove(be);
        }
    }

    public void getBeliever(Believer be){
        synchronized (believers){
            //选一个大主教
            System.out.println(believers.get(0));
        }
    }

    /**
     * 倾听每位信徒倾述
     */
    public void listenEveryBeliever(){

        List<Believer> believersBat = null;
        synchronized (believers){
            //快照副本
            believersBat = Lists.newArrayList(believers);
        }
        for(Believer be : believersBat){
            be.confide();
        }
    }

}
