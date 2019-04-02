package com.example.demo.redis;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@RunWith(SpringRunner.class)
@SpringBootTest
public class LuaTest {

    private MockMvc mockMvc;

    /**
     * 注入WebApplicationContext
     */
    @Autowired
    private WebApplicationContext wac;

    @Before // 在测试开始前初始化工作
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 问题太多，慎用
     */
    //@Resource
   // private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void test2(){
        stringRedisTemplate.opsForValue().set("hei","scha");
    }

    @Test
    public void test1(){

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<Long>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/rateLimitReq.lua")));

        //keys
        List<String> keyList = new ArrayList<String>();
        keyList.add("rate.limit:/luaLimit/getHello");

        //ARGV
        //List<String> argvList = Lists.newArrayList("10");
        String[] argvs = new String[]{"10"};
        //Integer key1 = (Integer) redisTemplate.opsForValue().get("rate.limit:/luaLimit/getHello");
        //System.out.println(key1);

        Long res = (Long) stringRedisTemplate.execute(redisScript,keyList,argvs);
        System.out.println(res);

    }

    @Test
    public void test3(){

        DefaultRedisScript<Long>  redisScript = new DefaultRedisScript<Long>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/getParam.lua")));

        //keys
        List<String> keyList = new ArrayList<String>();
        keyList.add("rate.limit:/luaLimit/getHello");
        //ARGV
        String[] argvs = new String[]{"10","20"};
        Long res = (Long) stringRedisTemplate.execute(redisScript,keyList,argvs);
        System.out.println(res);
    }

    @Test
    public void test4(){

        DefaultRedisScript<List>  redisScript = new DefaultRedisScript<List>();
        redisScript.setResultType(List.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/getParam.lua")));

        //keys
        List<String> keyList = new ArrayList<String>();
        keyList.add("rate.limit:/luaLimit/getHello");
        //ARGV
        List res = (List) stringRedisTemplate.execute(redisScript,keyList,"10");
        System.out.println(res);
    }

    @Test
    public void test5() throws Exception {
        String url = "/luaLimit/getHello";
        for (int i = 0; i < 20; i++) {
            sendReq(i, url);
        }
    }

    private void sendReq(int batchNo, String url) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .accept(MediaType.TEXT_HTML_VALUE))
                .andReturn();
        MockHttpServletResponse result = mvcResult.getResponse();
        System.out.println("Thread" + Thread.currentThread().getName() + ">>>batchNo=" + batchNo + ":" + result.getContentAsString());
    }
}
