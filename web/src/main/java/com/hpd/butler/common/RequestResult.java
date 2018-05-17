package com.hpd.butler.common;

/**
 * Created by zy on 2017/10/9.
 */
import java.io.Serializable;

public class RequestResult implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean success;
    private String errMsg;
    private Object data;

    public static RequestResult fail(String errMsg){
        RequestResult m = new RequestResult();
        m.success = false;
        m.errMsg = errMsg;
        m.data = null;

        return m;
    }

    public static RequestResult fail(String errMsg, Object errObj){
        RequestResult m = new RequestResult();
        m.success = false;
        m.data = errObj;
        m.errMsg = errMsg;

        return m;
    }

    public static RequestResult success(Object result){
        RequestResult m = new RequestResult();
        m.success = true;
        m.data = result;
        m.errMsg = "";

        return m;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

