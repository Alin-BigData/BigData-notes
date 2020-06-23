package com.wangfulin.features.java8;

import java.util.*;

import com.wangfulin.features.common.Employee;
import org.junit.Test;

/**
 * @projectName: newfuturesbaseon8ormore
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-06-10 10:54
 **/
public class LambdaExample {

    @Test
    public void test1() {
        // 匿名内部类
        Runnable r1 = new Runnable() {
            public void run() {
                System.out.println("hello");
            }
        };
        // lambda
        Runnable r2 = () -> System.out.println("hello lambda");

        new Thread(r1).start();
        new Thread(r2).start();
    }

    @Test
    public void test2() {
        // 使用匿名内部类作为参数传递
        TreeSet<String> ts = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                // 如果返回值为负数意味着o1比o2小，
                // 否则返回为零意味着o1等于o2，返回为正数意味着o1大于o2
                //return Integer.compare(o1.length(), o2.length());
                return o1.compareTo(o2);
            }
        });

        TreeSet<String> ts2 = new TreeSet<>(
                (x, y) -> Integer.compare(x.length(), y.length())
        );
        ts2.add("w");
        ts2.add("xx");
        System.out.printf(ts2.toString());
    }

    //需求：获取公司中年龄小于 35 的员工信息
    List<Employee> emps = Arrays.asList(
            new Employee(101, "张三", 18, 9999.99),
            new Employee(102, "李四", 59, 6666.66),
            new Employee(103, "王五", 28, 3333.33),
            new Employee(104, "赵六", 8, 7777.77),
            new Employee(105, "田七", 38, 5555.55)
    );


    public List<Employee> filterEmployees(List<Employee> list) {
        List<Employee> listx = new ArrayList<>();

        for (Employee emp : emps) {
            if (emp.getAge() <= 35) {
                listx.add(emp);
            }
        }

        return listx;
    }

    @Test
    public void test3() {
        List<Employee> list = filterEmployees(emps);

        for (Employee employee : list) {
            System.out.println(employee);
        }
    }

    //需求：获取公司中工资大于 5000 的员工信息
    public List<Employee> filterEmployeeSalary(List<Employee> emps) {
        List<Employee> list = new ArrayList<>();

        for (Employee emp : emps) {
            if (emp.getSalary() >= 5000) {
                list.add(emp);
            }
        }

        return list;
    }

    @Test
    public void test4() {
        // 用接口 将需要采用的方法传入
        //List<Employee> list = filterEmployee(emps,new FilterEmployeeForAge());
        List<Employee> list = filterEmployee(emps, new FilterEmployeeForSalary());


        for (Employee employee : list) {
            System.out.println(employee);
        }
    }

    // 以上两个需求 只有一个地方改变，因此可以采用一些方法优化

    // 优化方式一：策略设计模式
    public List<Employee> filterEmployee(List<Employee> emps, MyPredicate<Employee> mp) {
        List<Employee> list = new ArrayList<>();

        for (Employee employee : emps) {
            if (mp.test(employee)) {
                list.add(employee);
            }
        }
        return list;
    }

    // 优化方式二：匿名内部类
    @Test
    public void test5() {
        List<Employee> list = filterEmployee(emps, new MyPredicate<Employee>() {
            @Override
            public boolean test(Employee employee) {
                return employee.getSalary() >= 5000;
            }
        });

        for (Employee employee : list) {
            System.out.println(employee);
        }
    }

    //优化方式三：Lambda 表达式
    @Test
    public void test6() {
        List<Employee> list = filterEmployee(emps, (e) -> e.getSalary() >= 5000);
        list.forEach(System.out::println);
    }

    // 优化方式四：Stream API
    @Test
    public void test7() {
        emps.stream().filter((e)->e.getSalary() >= 5000).forEach(System.out::println);
    }

}
