package com.example.demo;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RateLimitTest {

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


    @Test
    public void testAcquire() {
        // acquire(i); 获取令牌，返回阻塞的时间,支持预消费
        RateLimiter limiter = RateLimiter.create(1);

        for (int i = 1; i < 10; i++) {
            double waitTime = limiter.acquire(i);
            System.out.println("cutTime=" + System.currentTimeMillis() + " acq:" + i + " waitTime:" + waitTime);
        }
    }

    @Test
    public void mulitplyReq() throws Exception {
        //String url = "/rateLimit/getHello";
        String url = "/calculate/getHello";
        for(int i = 0;i < 1000;i++){
            sendReq(i,url);
        }
    }

    @Test
    public void threadReq() throws Exception {
        String url = "/rateLimit/getHello";
        new Thread(()->{
            for(int i = 0;i < 500;i++){
                try {
                    sendReq(i,url);
                } catch (Exception e) {
                }
            }
        }).start();

        new Thread(()->{
            for(int i = 0;i < 500;i++){
                try {
                    sendReq(i,url);
                } catch (Exception e) {
                }
            }
        }).start();

        while(true){
            Thread.sleep(60 * 1000);
            break;
        }
    }


    private void sendReq(int batchNo,String url) throws Exception {
        /**
         * 1、mockMvc.perform执行一个请求。
         * 2、MockMvcRequestBuilders.get("XXX")构造一个请求。
         * 3、ResultActions.param添加请求传值
         * 4、ResultActions.accept(MediaType.TEXT_HTML_VALUE))设置返回类型
         * 5、ResultActions.andExpect添加执行完成后的断言。
         * 6、ResultActions.andDo添加一个结果处理器，表示要对结果做点什么事情
         *   比如此处使用MockMvcResultHandlers.print()输出整个响应结果信息。
         * 5、ResultActions.andReturn表示执行完成后返回相应的结果。
         */

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .accept(MediaType.TEXT_HTML_VALUE))
                // .andExpect(MockMvcResultMatchers.status().isOk())             //等同于Assert.assertEquals(200,status);
                // .andExpect(MockMvcResultMatchers.content().string("hello lvgang"))    //等同于 Assert.assertEquals
                // ("hello lvgang",content);
                //.andDo(MockMvcResultHandlers.print())//打印
                .andReturn();
        MockHttpServletResponse result = mvcResult.getResponse();
        System.out.println("Thread"+Thread.currentThread().getName()+">>>batchNo="+batchNo+":"+result.getContentAsString());
    }

    @Test
    public void test(){
        LocalDateTime beginTime = LocalDateTime.now();

        try {
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Long timeConsuming = Duration.between(beginTime,LocalDateTime.now()).toMillis();
        System.out.println(timeConsuming);
    }


}
