package com.wangfulin.features.java8;

import com.wangfulin.features.common.Employee;

/**
 * @projectName: newfuturesbaseon8ormore
 * @description: MyPredicate实现
 * @author: Wangfulin
 * @create: 2020-06-10 20:42
 **/
public class FilterEmployeeForAge implements MyPredicate<Employee>{


    @Override
    public boolean test(Employee employee) {
        return employee.getAge() <= 35;
    }
}
