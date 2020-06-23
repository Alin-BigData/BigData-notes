package com.wangfulin.features.java8;

/**
 * @projectName: newfuturesbaseon8ormore
 * @description: 接口 具体实现是采用什么方式过滤
 * @author: Wangfulin
 * @create: 2020-06-10 20:41
 **/
public interface MyPredicate<T> {
    public boolean test(T t);
}
