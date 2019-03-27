package com.example.demo.constant;

/**
 * 自定义响应码
 */
public enum ResponseEnum {
    OK(200, "成功"),
    RATE_LIMIT(403, "访问次数受限"),
    AUTHENTICATION_FAIL(401, "未授权"),
    SERVER_ERROR(500, "服务器错误");

    private int code;

    private String mesg;

    ResponseEnum(int code, String mesg) {
        this.code = code;
        this.mesg = mesg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMesg() {
        return mesg;
    }

    public void setMesg(String mesg) {
        this.mesg = mesg;
    }

    @Override
    public String toString() {
        return "ResponseEnum{" +
                "code=" + code +
                ", mesg='" + mesg + '\'' +
                '}';
    }
}
