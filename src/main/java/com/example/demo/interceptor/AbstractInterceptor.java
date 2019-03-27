package com.example.demo.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.IOUtils;
import com.example.demo.constant.ResponseEnum;
import com.example.demo.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class AbstractInterceptor extends HandlerInterceptorAdapter {
    public final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 具体拦截方法
     *
     * @param req
     * @return
     */
    protected abstract ResponseEnum handleRequest(HttpServletRequest req) throws Exception;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ResponseEnum result = ResponseEnum.SERVER_ERROR;
        try {
            result = handleRequest(request);
        } catch (Exception e) {
            log.error("handleRequest catch an Exception>>>", e);
        }

        if (ResponseEnum.OK == result) {
            //成功
            return true;
        }

        handleFailResponse(response, result);
        return false;
    }

    public void handleFailResponse(HttpServletResponse resp, ResponseEnum result) {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        ResponseDTO dto = new ResponseDTO();
        dto.setCode(result.getCode());
        dto.setMesg(result.getMesg());
        PrintWriter pw = null;
        try {
            pw = resp.getWriter();
            pw.write(JSON.toJSONString(dto));
        } catch (IOException e) {
            log.error("handleFailResponse catch an IOException>>>", e);
        } finally {
            IOUtils.close(pw);
        }
    }
}
