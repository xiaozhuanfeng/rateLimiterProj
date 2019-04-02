package com.example.demo.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTemplateTest {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private ValueOperations getOpsForValue(){
        return stringRedisTemplate.opsForValue();
    }

    @Test
    public void expire1(){
        getOpsForValue().set("userName","Jack");

        //1.RedisTemplate.expire(String key, long timeout, TimeUnit unit)
        //设置key的失效时间，timeout是时间参数，timeunit是时间单位，返回值是布尔
        stringRedisTemplate.expire("userName",5,TimeUnit.SECONDS);
        System.out.println("userName="+getOpsForValue().get("userName"));//userName=Jack

        try {
            Thread.sleep(5000);
            System.out.println("userName="+getOpsForValue().get("userName"));//userName=null
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void expire2(){
        getOpsForValue().set("userName","Jack");

        //2.RedisTemplate.expireAt(String key, Date date)
        //设置key在一个时间点失效，返回值是布尔
        LocalDateTime today = LocalDateTime.now();

        //5S后
        Date date = localDateTimeToDate(today.plusSeconds(5));
        stringRedisTemplate.expireAt("userName",date);
        System.out.println("userName="+getOpsForValue().get("userName"));//userName=Jack

        //3.RedisTemplate.getExpire(String key)  获取key的存活时间，返回值是long
        System.out.println(stringRedisTemplate.getExpire("userName"));

        //4.RedisTemplate.getExpire(String key, TimeUnit timeUnit)
        //获取key的存活时间，加上了时间单位，返回值是long
        System.out.println(stringRedisTemplate.getExpire("userName",TimeUnit.SECONDS));

        try {
            Thread.sleep(6000);
            System.out.println("userName="+getOpsForValue().get("userName"));//userName=null
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void SortQuery(){
      /*  stringRedisTemplate.opsForZSet().add("adc", "破败之刃", 1.0);
        stringRedisTemplate.opsForZSet().add("adc", "电刀", 2.0);
        stringRedisTemplate.opsForZSet().add("adc", "饮血刀", 3.0);
        stringRedisTemplate.opsForZSet().add("adc", "无尽之刃", 4.0);
        stringRedisTemplate.opsForZSet().add("adc", "多兰之剑", 0.0);*/

        System.out.println(stringRedisTemplate.opsForZSet().range("adc",0,-1));

        SortQuery<String> query = SortQueryBuilder.sort("adc")// 排序的key
                //.by("adc->*")     //  key的正则过滤
                //.noSort()            //不使用正则过滤key
                //.get("")            //在value里过滤正则，可以连续写多个get
                .limit(0, 3)         //分页，和mysql一样
                .order(SortParameters.Order.DESC)   //正序or倒序
                .alphabetical(true)  //ALPHA修饰符用于对字符串进行排序，false的话只针对数字排序
                .build();

        List<String> list= stringRedisTemplate.sort(query);

        System.out.println(list);
    }

    private Date localDateTimeToDate(LocalDateTime localDateTime ){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }
}
