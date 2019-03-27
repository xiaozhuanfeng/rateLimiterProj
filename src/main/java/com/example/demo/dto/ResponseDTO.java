package com.example.demo.dto;

public class ResponseDTO {
    private int code;

    private String mesg;

    public ResponseDTO() {
    }

    public ResponseDTO(int code, String mesg) {
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
        return "ResponseDTO{" +
                "code=" + code +
                ", mesg='" + mesg + '\'' +
                '}';
    }
}
