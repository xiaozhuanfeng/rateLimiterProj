package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.example.demo.constant.ResponseEnum;
import com.example.demo.dto.ResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JsonTest {

    @Test
    public void dto2JsonMapper1(){
        ResponseEnum result = ResponseEnum.OK;
        ObjectMapper objMapper = new ObjectMapper();
        try {
            String json = objMapper.writeValueAsString(result);
            System.out.println(json); //"OK"
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dto2JsonMapper2(){
        ResponseEnum result = ResponseEnum.OK;
        ResponseDTO dto = new ResponseDTO();
        dto.setCode(result.getCode());
        dto.setMesg(result.getMesg());

        ObjectMapper objMapper = new ObjectMapper();
        try {
            String json = objMapper.writeValueAsString(dto);
            System.out.println(json); //{"code":200,"mesg":"成功"},objMapper dto 空属性，将返回 "xxx"=null
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void dto2Json1(){
        ResponseEnum result = ResponseEnum.OK;
        String json = JSON.toJSONString(result);
        System.out.println(json);//"OK"
    }

    @Test
    public void dto2Json2(){
        ResponseEnum result = ResponseEnum.OK;
        ResponseDTO dto = new ResponseDTO();
        dto.setCode(result.getCode());
        dto.setMesg(result.getMesg());

        String json = JSON.toJSONString(dto);
        System.out.println(json);//{"code":200,"mesg":"成功"}  与区别在空属性
    }
}
