package com.example.redisdemo.common;


import lombok.Data;

@Data
public class Result<T> {
    private boolean success=false;
    private String message = "";
    private T data = null;

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public boolean isSuccess() {
        return success;
    }

    public Result() {
    }

    public static <T> Result<T> errorResult(String msg) {
        Result<T> result = new Result<T>();
        result.setSuccess(false);
        result.setMessage(msg);
        return result;
    }


    public static <T> Result<T> successResult(T data) {
        Result<T> result = new Result<T>();
        result.setSuccess(true);
        result.setData(data);
        if(data instanceof String) {
            result.setMessage((String)data);
        }else{
            result.setMessage("成功");
        }

        return result;
    }

    public static <T> Result<T> successResult(T data,String msg) {
        Result<T> result = new Result<T>();
        result.setSuccess(true);
        result.setData(data);
        result.setMessage(msg);
        return result;
    }



    public static <T> Result<T> successResult() {
        Result<T> result = new Result<T>();
        result.setSuccess(true);
        result.setData(null);
        result.setMessage("成功");
        return result;
    }

}

