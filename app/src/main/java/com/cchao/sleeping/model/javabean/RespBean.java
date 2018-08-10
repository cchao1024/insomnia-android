package com.cchao.sleeping.model.javabean;

import com.cchao.sleeping.global.Constants;

/**
 * description 服务器响应的数据
 * author  cchao
 * date  2017/2/24
 **/
public class RespBean<T> {

    private String code;
    private T result;
    private String extend;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isCodeSuc() {
        return code.equals(Constants.ApiResp.CODE_SUC);
    }
}
