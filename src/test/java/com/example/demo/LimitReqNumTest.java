package com.example.demo;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableScheduling
public class LimitReqNumTest {
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
    public void testHash() {
        String uri = "/calculate/getHello";
        System.out.println(uri.hashCode());
    }

    @Test
    public void testCache() throws ExecutionException {

        //maximumWeight 限制缓存占据内存大小
        //maximumSize 缓存的个数并不是缓存占据内存的大小，当缓存的数量超过了maximumSize时,guava cache会要据LRU算法淘汰掉最近没有写入或访问的数据
        LoadingCache<String, AtomicLong> cache =
                CacheBuilder.newBuilder().expireAfterWrite(60 * 10, TimeUnit.SECONDS).maximumWeight(10).build(new CacheLoader<String,
                        AtomicLong>() {
                    //本地缓存没有命中时，调用load，并将结果缓存
                    @Override
                    public AtomicLong load(String aLong) throws Exception {
                        return new AtomicLong(0L);
                    }
                });
        cache.put("/calculate/getHello", new AtomicLong(0L));
        cache.put("/calculate/getHello2", new AtomicLong(0L));

        AtomicLong atomic = (AtomicLong) cache.get("/calculate/getHello");
        atomic.incrementAndGet();
        AtomicLong atomic2 = (AtomicLong) cache.get("/calculate/getHello");
        System.out.println(atomic2.get());

        AtomicLong atomic3 = (AtomicLong) cache.get("/calculate/getHello2");
        System.out.println(atomic3.get());

        AtomicLong atomic4 = (AtomicLong) cache.get("/calculate/getHello3");
        System.out.println(atomic4);

        AtomicLong atomic5 = (AtomicLong) cache.get("/calculate/getHello3");
        System.out.println(atomic5.get());
    }

    @Test
    public void mulitplyReq() throws Exception {
        String url = "/numLimit/getHello";
        //String url = "/calculate/getHello";
        for (int i = 0; i < 1000; i++) {
            sendReq(i, url);
        }
    }

    @Test
    public void mulitplyReq2() throws Exception {
        String url = "/numLimit/getHello";
        //String url = "/calculate/getHello";
        for (int i = 0; i < 100; i++) {
            sendReq(i, url);
        }

        String url2 = "/numLimit/getHello2";
        for (int i = 0; i < 15; i++) {
            sendReq(i, url2);
        }

        Thread.sleep(15 * 1000L);
        for (int i = 0; i < 15; i++) {
            sendReq(i, url);
        }
    }

    private void sendReq(int batchNo, String url) throws Exception {
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
        System.out.println("Thread" + Thread.currentThread().getName() + ">>>batchNo=" + batchNo + ":" + result.getContentAsString());
    }

}
