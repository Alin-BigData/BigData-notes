package com.wangfulin.ch8a.vo;

/**
 * @projectName: concurrent
 * @description: 方法本身运行是否正确的结果类型
 * @author: Wangfulin
 * @create: 2020-04-22 23:58
 **/
public enum TaskResultType {
    //方法成功执行并返回了业务人员需要的结果
    Success,
    //方法成功执行但是返回的是业务人员不需要的结果
    Failure,
    //方法执行抛出了Exception
    Exception;
}
