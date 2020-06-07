package com.wangfulin.vueblog.common;

import lombok.Data;

/**
 * @projectName: vueblog
 * @description: 统一结果
 * @author: Wangfulin
 * @create: 2020-06-06 17:04
 **/
@Data
public class Result {
    private int code;
    private String msg;
    private Object data;

    public static Result success(Object data) {
        Result re = new Result();
        re.setCode(200);
        re.setData(data);
        re.setMsg("操作成功");
        return re;
    }

    public static Result success(int code, String msg, Object data) {
        Result re = new Result();
        re.setCode(code);
        re.setData(data);
        re.setMsg(msg);
        return re;
    }


    public static Result fail(String msg) {
        return fail(400, msg, null);
    }

    public static Result fail(String msg, Object data) {
        return fail(400, msg, data);
    }

    public static Result fail(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
}
