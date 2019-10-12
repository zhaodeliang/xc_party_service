package com.daling.party.infrastructure.model;

import java.io.Serializable;

public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = 1870230157699806964L;
    private boolean retBool;
    private int total;
    private String message;
    private int code;
    private T t;
    private String other;

    public ResultVO() {
        super();
    }

    public ResultVO(boolean retBool, String message) {
        super();
        this.retBool = retBool;
        this.message = message;
    }

    public ResultVO(boolean retBool, String message, T t) {
        super();
        this.retBool = retBool;
        this.message = message;
        this.t = t;
    }

    public ResultVO<T> format(boolean retBool, String message, T t) {
        this.retBool = retBool;
        this.message = message;
        this.t = t;
        return this;
    }

    public ResultVO<T> format(boolean retBool, String message) {
        this.retBool = retBool;
        this.message = message;
        return this;
    }

    public ResultVO<T> format(boolean retBool, String message, T t, int code) {
        this.retBool = retBool;
        this.message = message;
        this.t = t;
        this.code = code;
        return this;
    }

    public ResultVO<T> format(boolean retBool, String message, int code) {
        this.retBool = retBool;
        this.message = message;
        this.code = code;
        return this;
    }

    public static <T> ResultVO<T> newResult() {
        return new ResultVO<T>();
    }

    public boolean isRetBool() {
        return retBool;
    }

    public void setRetBool(boolean retBool) {
        this.retBool = retBool;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ResultVO [retBool=" + retBool + ", total=" + total + ", message=" + message + ", code=" + code + "]";
    }
}
