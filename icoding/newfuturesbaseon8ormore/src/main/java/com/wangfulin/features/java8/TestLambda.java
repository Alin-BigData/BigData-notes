package com.wangfulin.features.java8;

import com.wangfulin.features.common.Employee;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @projectName: newfuturesbaseon8ormore
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-06-11 10:11
 **/
public class TestLambda {

    List<Employee> emps = Arrays.asList(
            new Employee(101, "张三", 18, 9999.99),
            new Employee(102, "李四", 59, 6666.66),
            new Employee(103, "王五", 28, 3333.33),
            new Employee(104, "赵六", 8, 7777.77),
            new Employee(105, "田七", 38, 5555.55)
    );

    // 定制sort方法 年龄相同比名字
    @Test
    public void test1() {
        Collections.sort(emps, (e1, e2) -> {
            if (e1.getAge() == e2.getAge()) {
                return e1.getName().compareTo(e2.getName());
            } else {
                return Integer.compare(e1.getAge(), e2.getAge());
            }
        });
        for (Employee employee : emps) {
            System.out.println(employee);
        }
    }

    @Test
    public void test2() {
        String trimStr = strHandler("  ni", (e) -> e.trim());
    }


    // 处理字符串
    public String strHandler(String str, MyFunction mf) {
        return mf.getValue(str);
    }

    // 处理运算
    @Test
    public void test3() {
        op(100L, 200L, (x, y) -> x + y);
    }

    public void op(Long l1, Long l2, MyFunction2<Long, Long> mf) {
        System.out.println(mf.getValue(l1, l2));
    }

    @Test
    public void test4() {
        int re = operate(100, 200, (x, y) -> x + y);
        System.out.println(re);
    }

    // 定义接口 -》 操作函数 -》 传参调用
    public int operate (int a, int b, MathOperation my) {
        return my.operation(a,b);
    }


}

// 处理字符串接口
@FunctionalInterface
interface MyFunction {

    public String getValue(String str);

}

// T是参数类型 R是返回值类型
@FunctionalInterface
interface MyFunction2<T, R> {

    public R getValue(T t1, T t2);

}

interface MathOperation {
    int operation(int a, int b);
}
