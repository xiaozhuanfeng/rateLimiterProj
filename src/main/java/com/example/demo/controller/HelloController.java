package com.example.demo.controller;

import org.apache.juli.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value="/rateLimit/getHello",method = RequestMethod.GET)
    public String getHello(){
        log.info("rateLimit Hello ........");

        try {
            //发呆1s
            Thread.sleep(100);
        } catch (InterruptedException e) {
            log.error("getHello catch an InterruptedException>>",e);
        }

        return "Get:Hello,World";
    }

    @RequestMapping(value="/calculate/getHello",method = RequestMethod.GET)
    public String postHello(){
        log.info("calculate Hello ........");

        try {
            //发呆1s
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("getHello catch an InterruptedException>>",e);
        }

        return "Post:Hello,World";
    }

    @RequestMapping(value="/numLimit/getHello",method = RequestMethod.GET)
    public String sayHello(){
        log.info("numLimit Hello ........");

        try {
            //发呆1s
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("getHello catch an InterruptedException>>",e);
        }
        return "Say:Hello,World";
    }

    @RequestMapping(value="/numLimit/getHello2",method = RequestMethod.GET)
    public String sayHello2(){
        log.info("numLimit Hello2 ........");

        try {
            //发呆1s
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("getHello catch an InterruptedException>>",e);
        }
        return "Say:Hello,World";
    }

    @RequestMapping(value="/luaLimit/getHello",method = RequestMethod.GET)
    public String luaHello(){
        log.info("luaLimit Hello2 ........");
        try {
            //发呆1s
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("getHello catch an InterruptedException>>",e);
        }
        return "Say:Hello,World";
    }
}
